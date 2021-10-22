package com.loginov.shorturl.service;

import com.loginov.shorturl.dto.UrlDto;
import com.loginov.shorturl.model.UrlEntity;
import com.loginov.shorturl.repository.UrlRepo;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class UrlService {

    private final UrlRepo urlRepo;

    public UrlService(UrlRepo urlRepo) {
        this.urlRepo = urlRepo;
    }

    public List<UrlEntity> getAll() {
        return urlRepo.findAll();
    }

    public UrlEntity findByFullUrl(String fullUrl, ZonedDateTime time){
        return urlRepo.findUrlEntityByOriginalUrlAndExpiresTime(fullUrl,time);
    }

    public UrlEntity findUrlByShortUrl(String shorter, ZonedDateTime time) {
        return urlRepo.findUrlByShortUrl(shorter, time);
    }

    public List<UrlEntity> findAllByShortUrl(String shortUrl){
        return urlRepo.findAllByShortUrl(shortUrl);
    }

    public UrlEntity generateShortLink(UrlDto urlDto) throws NoSuchAlgorithmException {
        String encodedUrl = encodedUrl(urlDto.getUrl());

        UrlEntity urlToSave = new UrlEntity();
        urlToSave.setCreatedTime(ZonedDateTime.now());
        urlToSave.setOriginalUrl(urlDto.getUrl());
        urlToSave.setShortUrl(encodedUrl);
        urlToSave.setExpiresTime(urlToSave.getCreatedTime().plusMinutes(1));

        return saveShortLink(urlToSave);
    }

    private String encodedUrl(String url) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(url.getBytes());
        byte[] digest = md.digest();
        String fullEncodeUrl = DatatypeConverter.printHexBinary(digest);
        return fullEncodeUrl.substring(0, 6);
    }

    public UrlEntity saveShortLink(UrlEntity urlEntity) {
        return urlRepo.save(urlEntity);
    }
}
