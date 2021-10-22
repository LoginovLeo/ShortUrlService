package com.loginov.shorturl.dto;

import java.time.ZonedDateTime;

public class UrlRespDto {

    private String shortUrl;
    private ZonedDateTime expireDate;


    public UrlRespDto() {
    }

    public UrlRespDto(String shortUrl, ZonedDateTime expireDate) {
        this.shortUrl = shortUrl;
        this.expireDate = expireDate;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(ZonedDateTime expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "UrlRespDto{" +
                "shortUrl='" + shortUrl + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }
}
