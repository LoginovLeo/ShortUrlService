package com.example.shorturl.controller;

import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.service.UrlService;
import netscape.javascript.JSObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@RestController()
@RequestMapping("/url")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping()
    public List<UrlEntity> getAll() {
        return urlService.getAll();
    }

    @GetMapping("{url}")
    public ResponseEntity<?> findByShortUrl(@PathVariable String url) {
        UrlEntity urlByShortUrl = urlService.findUrlByShortUrl(url);
        if (urlByShortUrl != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", urlByShortUrl.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("Url with shorter url " + url + " not found", HttpStatus.NOT_FOUND);
        }
    }
}
