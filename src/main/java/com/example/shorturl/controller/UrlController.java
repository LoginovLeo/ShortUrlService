package com.example.shorturl.controller;

import com.example.shorturl.dto.UrlDto;
import com.example.shorturl.dto.UrlErrorDto;
import com.example.shorturl.dto.UrlRespDto;
import com.example.shorturl.exeption.MissingParamUrl;
import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.service.UrlService;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> createShortUrl(@RequestBody UrlDto urlDto) throws NoSuchAlgorithmException {
        if (urlDto.getUrl() == null) {
            throw new MissingParamUrl("Missing param url in request body");
        }

        if (new UrlValidator().isValid(urlDto.getUrl())) {
            UrlEntity byFullUrl = urlService.findByFullUrl(urlDto.getUrl(), ZonedDateTime.now());

            if (byFullUrl == null) {
                UrlEntity urlEntity = urlService.generateShortLink(urlDto);

                UrlRespDto respDto = new UrlRespDto();
                respDto.setShortUrl(urlEntity.getShortUrl());
                respDto.setExpireDate(urlEntity.getExpiresTime());

                return new ResponseEntity<>(respDto, HttpStatus.OK);
            }

            UrlRespDto respDto = new UrlRespDto();
            respDto.setShortUrl(byFullUrl.getShortUrl());
            respDto.setExpireDate(byFullUrl.getExpiresTime());

            return new ResponseEntity<>(respDto, HttpStatus.OK);

        }

        UrlErrorDto errorDto = new UrlErrorDto("400",
                "The parameter " + urlDto.getUrl() + " in the request body is not url. Please try again",
                ZonedDateTime.now());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
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
                UrlErrorDto errorDto = new UrlErrorDto(HttpStatus.NOT_FOUND.toString(),
                        "Url with shorter url " + shortUrl + "  doesn't exist.",
                        ZonedDateTime.now());
                return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
            }

            UrlErrorDto errorDto = new UrlErrorDto(HttpStatus.BAD_REQUEST.toString(),
                    "Shorter url " + shortUrl + " expired. Please create new one",
                    ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new UrlDto(activeUrl.getOriginalUrl()), HttpStatus.OK);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectByShortUrl(@PathVariable String shortUrl) {
        if (shortUrl.isBlank()) {
            UrlErrorDto errorDto = new UrlErrorDto(HttpStatus.BAD_REQUEST.toString(), "Url is empty", ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        UrlEntity urlByShortUrl = urlService.findUrlByShortUrl(shortUrl, ZonedDateTime.now());

        if (urlByShortUrl == null) {
            UrlErrorDto errorDto = new UrlErrorDto(HttpStatus.BAD_REQUEST.toString(),
                    "Url with shorter url " + shortUrl + " expired or doesn't exist",
                    ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlByShortUrl.getOriginalUrl());
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
}
