package com.loginov.shorturl.controller;

import com.loginov.shorturl.dto.UrlDto;
import com.loginov.shorturl.dto.UrlRespDto;
import com.loginov.shorturl.exeption.customexceptions.CustomBadRequestException;
import com.loginov.shorturl.exeption.customexceptions.CustomNotFoundException;
import com.loginov.shorturl.model.UrlEntity;
import com.loginov.shorturl.service.RequestService;
import com.loginov.shorturl.service.UrlService;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/url")
public class UrlController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlController.class);



    private final UrlService urlService;

    private final RequestService requestService;



    public UrlController(UrlService urlService, RequestService requestService) {
        this.urlService = urlService;
        this.requestService = requestService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> createShortUrl(HttpServletRequest request, HttpServletResponse response, @RequestBody UrlDto urlDto) throws NoSuchAlgorithmException {

        requestService.chekSessions();



        if (urlDto.getUrl() == null) {
            throw new CustomNotFoundException("Missing param: 'url' in request body" );
        }

        if (new UrlValidator().isValid(urlDto.getUrl())) {
            UrlEntity byFullUrl = urlService.findByFullUrl(urlDto.getUrl(), ZonedDateTime.now());

            if (byFullUrl == null) {
                UrlEntity urlEntity = urlService.generateShortLink(urlDto);

                UrlRespDto respDto = new UrlRespDto();
                respDto.setShortUrl(urlEntity.getShortUrl());
                respDto.setExpireDate(urlEntity.getExpiresTime());

                LOGGER.info("Generate new short URL: " + urlEntity.getShortUrl());

                return new ResponseEntity<>(respDto, HttpStatus.OK);
            }

            UrlRespDto respDto = new UrlRespDto();
            respDto.setShortUrl(byFullUrl.getShortUrl());
            respDto.setExpireDate(byFullUrl.getExpiresTime());

            LOGGER.info("Get exist short URL: " + byFullUrl.getShortUrl());

            return new ResponseEntity<>(respDto, HttpStatus.OK);

        }


        throw new CustomBadRequestException(
                "The parameter: '" + urlDto.getUrl() + "' in  request body is not url. Please try again");
    }

    @GetMapping("/getUrl/{shortUrl}")
    public ResponseEntity<?> getUrlByShortUrl(@PathVariable String shortUrl) {
        List<UrlEntity> allByShortUrl = urlService.findAllByShortUrl(shortUrl);
        UrlEntity activeUrl = allByShortUrl.stream()
                .filter(e -> e.getExpiresTime().isAfter(ZonedDateTime.now()))
                .findFirst()
                .orElse(null);

        if (activeUrl == null) {
            List<UrlEntity> expiredUrl = allByShortUrl.stream()
                    .filter(e -> e.getExpiresTime().isBefore(ZonedDateTime.now()))
                    .collect(Collectors.toList());

            if (expiredUrl.isEmpty()) {
                throw new CustomNotFoundException("Url with shorter url " + shortUrl + "  doesn't exist.");
            }

            throw new CustomBadRequestException("Shorter url " + shortUrl + " expired. Please create new one");
        }

        return new ResponseEntity<>(new UrlDto(activeUrl.getOriginalUrl()), HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectByShortUrl(@PathVariable String shortUrl) {
        if (shortUrl.isBlank()) {
           throw new CustomBadRequestException("Url is empty");
        }

        UrlEntity urlByShortUrl = urlService.findUrlByShortUrl(shortUrl, ZonedDateTime.now());

        if (urlByShortUrl == null) {
           throw new CustomBadRequestException("Url with shorter url " + shortUrl + " expired or doesn't exist");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlByShortUrl.getOriginalUrl());
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
}
