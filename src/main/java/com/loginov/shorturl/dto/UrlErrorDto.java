package com.loginov.shorturl.dto;

import java.time.ZonedDateTime;

public class UrlErrorDto {

    private String status;
    private String errorMsg;
    private ZonedDateTime timestamp;

    public UrlErrorDto(String status, String errorMsg, ZonedDateTime timestamp) {
        this.status = status;
        this.errorMsg = errorMsg;
        this.timestamp = timestamp;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
