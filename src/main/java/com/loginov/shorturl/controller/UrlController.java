package com.loginov.shorturl.controller;

import com.loginov.shorturl.dto.UrlDto;
import com.loginov.shorturl.dto.UrlRespDto;
import com.loginov.shorturl.exeption.customexceptions.CustomBadRequestException;
import com.loginov.shorturl.exeption.customexceptions.CustomNotFoundException;
import com.loginov.shorturl.model.UrlEntity;
import com.loginov.shorturl.service.RequestService;
import com.loginov.shorturl.service.TimeFormatter;
import com.loginov.shorturl.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("/api")
@Tag(name = "Url controller", description = "main controller")
public class UrlController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UrlController.class);

    private final UrlService urlService;
    private final RequestService requestService;

    public UrlController(UrlService urlService, RequestService requestService, TimeFormatter timeFormatter) {
        this.urlService = urlService;
        this.requestService = requestService;
    }

    @PostMapping("/generate")
    @Operation(
            summary = "Create Short Url",
            description = "Allows to generate short url"
    )
    public ResponseEntity<?> createShortUrl(
            @RequestBody @Parameter(description = "Full URL for generate short URL") UrlDto urlDto) throws NoSuchAlgorithmException {

        requestService.chekSessions();

        if (urlDto.getUrl() == null) {
            throw new CustomBadRequestException("Missing param: 'url' in request body");
        }

        if (new UrlValidator().isValid(urlDto.getUrl())) {
            UrlEntity byFullUrl = urlService.findByFullUrl(urlDto.getUrl(), ZonedDateTime.now(ZoneOffset.UTC));

            if (byFullUrl == null) {
                UrlEntity urlEntity = urlService.generateShortLink(urlDto);

                UrlRespDto respDto = new UrlRespDto();
                respDto.setShortUrl(urlEntity.getShortUrl());
                respDto.setExpireDate(urlEntity.getExpiresTime());

                LOGGER.info("Generate new short URL: " +
                        urlEntity.getShortUrl() +
                        "for original url " +
                        urlDto.getUrl());

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
    @Operation(
            summary = "Get URL by short URL",
            description = "Allows to get full URL by generated URL"
    )
    public ResponseEntity<?> getUrlByShortUrl(
            @PathVariable @Parameter(description = "Short URL for get full URL")String shortUrl) {

        List<UrlEntity> allByShortUrl = urlService.findAllByShortUrl(shortUrl);
        if (allByShortUrl.isEmpty()) {
            throw new CustomNotFoundException("Url with shorter url " + shortUrl + "  doesn't exist.");
        }

        UrlEntity activeUrl = urlService.getActive(allByShortUrl);
        if (activeUrl == null) {
            throw new CustomBadRequestException("Shorter url " + shortUrl + " expired. Please create new one");
        }

        LOGGER.info("Get url " + activeUrl.getOriginalUrl() + " by short url " + shortUrl);
        return new ResponseEntity<>(new UrlDto(activeUrl.getOriginalUrl()), HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    @Operation(
            summary = "Redirect",
            description = "Allows to redirect by short URL"
    )
    public ResponseEntity<?> redirectByShortUrl(
            @PathVariable @Parameter(description = "Short URL to redirect")String shortUrl) {

        if (shortUrl.isBlank()) {
            throw new CustomBadRequestException("Parameter 'shortUrl' is empty");
        }
        UrlEntity urlByShortUrl =
                urlService.findUrlEntityByOriginalUrlAndExpiresTime(shortUrl, ZonedDateTime.now(ZoneOffset.UTC));

        if (urlByShortUrl == null) {
            throw new CustomBadRequestException("Url with shorter url " +
                    "'" + shortUrl + "'" +
                    " expired or doesn't exist");
        }

        LOGGER.info("Redirect by short URL " + shortUrl + " to original url " + urlByShortUrl.getOriginalUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlByShortUrl.getOriginalUrl());
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
}