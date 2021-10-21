package com.example.shorturl.controller;

import com.example.shorturl.dto.UrlDto;
import com.example.shorturl.dto.UrlErrorDto;
import com.example.shorturl.dto.UrlRespDto;
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
            UrlErrorDto errorDto = new UrlErrorDto("400",
                    "Missing param \"url\":  in request body",
                    ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
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

    @GetMapping()
    public List<UrlEntity> getAll() {
        return urlService.getAll();
    }

    @GetMapping("/{url}")
    public ResponseEntity<?> redirectByShortUrl(@PathVariable String url) {
        if (url.isBlank()) {
            UrlErrorDto errorDto = new UrlErrorDto("400", "Url is empty", ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        UrlEntity urlByShortUrl = urlService.findUrlByShortUrl(url, ZonedDateTime.now());

        if (urlByShortUrl == null) {
            UrlErrorDto errorDto = new UrlErrorDto("400",
                    "Url with shorter url " + url + " expired or doesn't exist",
                    ZonedDateTime.now());
            return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlByShortUrl.getOriginalUrl());
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }
}
