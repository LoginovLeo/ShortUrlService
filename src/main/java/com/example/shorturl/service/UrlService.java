package com.example.shorturl.service;

import com.example.shorturl.model.UrlEntity;
import com.example.shorturl.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UrlService {

    private final UrlRepo urlRepo;

    @Autowired
    public UrlService(UrlRepo urlRepo) {
        this.urlRepo = urlRepo;
    }

    public List<UrlEntity> getAll() {
        return urlRepo.findAll();
    }
}
