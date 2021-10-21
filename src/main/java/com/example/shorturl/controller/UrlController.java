package com.example.shorturl.controller;

import com.example.shorturl.dto.UrlDto;
import com.example.shorturl.dto.UrlRespDto;
import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.service.UrlService;
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

    @PostMapping
    public ResponseEntity<?> createShortUrl(@RequestBody UrlDto urlDto) throws NoSuchAlgorithmException {
        UrlEntity byFullUrl = urlService.findByFullUrl(urlDto.getUrl(), ZonedDateTime.now());

        if (byFullUrl == null){
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

    @GetMapping()
    public List<UrlEntity> getAll() {
        return urlService.getAll();
    }

    @GetMapping("{url}")
    public ResponseEntity<?> redirectByShortUrl(@PathVariable String url) {
        UrlEntity urlByShortUrl = urlService.findUrlByShortUrl(url, ZonedDateTime.now());

        if (urlByShortUrl == null){
            return new ResponseEntity<>("Url with shorter url " + url + " expired", HttpStatus.NOT_FOUND);
        }

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", urlByShortUrl.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);


    }
}
