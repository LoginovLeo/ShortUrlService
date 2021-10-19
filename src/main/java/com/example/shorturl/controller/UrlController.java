package com.example.shorturl.controller;

import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.service.UrlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
