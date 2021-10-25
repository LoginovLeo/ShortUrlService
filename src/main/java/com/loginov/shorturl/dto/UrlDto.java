package com.loginov.shorturl.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entity of URL")
public class UrlDto {
    @Schema(description = "Full URL", example = "https://github.com/LoginovLeo/ShortUrlService")
    private String url;

    public UrlDto() {
    }

    public UrlDto(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
