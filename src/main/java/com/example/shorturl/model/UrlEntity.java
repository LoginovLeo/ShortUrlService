package com.example.shorturl.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Table(name = "url", indexes = {
        @Index(name = "url_short_url_key", columnList = "short_url", unique = true)
})
@Entity
public class UrlEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "original_url", nullable = false)
    private String originalUrl;


    @Column(name = "short_url", nullable = false)
    private String shortUrl;

    @Column(name = "created_time", nullable = false)
    private ZonedDateTime createdTime;

    public ZonedDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(ZonedDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}