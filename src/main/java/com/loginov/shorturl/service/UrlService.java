package com.loginov.shorturl.service;

import com.loginov.shorturl.dto.UrlDto;
import com.loginov.shorturl.model.UrlEntity;
import com.loginov.shorturl.repository.UrlRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UrlService {
    @Value("${url.expiredtime}")
    private int liveTime;

    private final UrlRepo urlRepo;

    public UrlService(UrlRepo urlRepo) {
        this.urlRepo = urlRepo;
    }

    public List<UrlEntity> getAll() {
        return urlRepo.findAll();
    }

    public UrlEntity findByFullUrl(String fullUrl, ZonedDateTime time) {
        return urlRepo.findUrlEntityByOriginalUrlAndExpiresTime(fullUrl, time);
    }

    public UrlEntity findUrlEntityByOriginalUrlAndExpiresTime(String shorter, ZonedDateTime time) {
        return urlRepo.findUrlByShortUrl(shorter, time);
    }

    public List<UrlEntity> findAllByShortUrl(String shortUrl) {
        return urlRepo.findAllByShortUrl(shortUrl);
    }

    public UrlEntity generateShortLink(UrlDto urlDto) throws NoSuchAlgorithmException {
        String encodedUrl = encodedUrl(urlDto.getUrl());

        UrlEntity urlToSave = new UrlEntity();
        urlToSave.setCreatedTime(ZonedDateTime.now(ZoneOffset.UTC));
        urlToSave.setOriginalUrl(urlDto.getUrl());
        urlToSave.setShortUrl(encodedUrl);
        urlToSave.setExpiresTime(urlToSave.getCreatedTime().plusMinutes(liveTime));

        return saveShortLink(urlToSave);
    }

    private String encodedUrl(String url) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(url.getBytes());
        byte[] digest = md.digest();
        String fullEncodeUrl = DatatypeConverter.printHexBinary(digest);
        return fullEncodeUrl.substring(0, 6);
    }

    public UrlEntity getActive(List<UrlEntity> urlEntities) {
        return urlEntities.stream()
                .filter(e -> e.getExpiresTime().isAfter(ZonedDateTime.now(ZoneOffset.UTC)))
                .findFirst()
                .orElse(null);
    }

    public List<UrlEntity> getExpired(List<UrlEntity> urlEntities) {
        return urlEntities.stream()
                .filter(e -> e.getExpiresTime().isBefore(ZonedDateTime.now(ZoneOffset.UTC)))
                .collect(Collectors.toList());
    }

    public UrlEntity saveShortLink(UrlEntity urlEntity) {
        return urlRepo.save(urlEntity);
    }
}
