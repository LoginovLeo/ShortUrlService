package com.loginov.shorturl.exeption;

import java.time.ZonedDateTime;

public class ErrorMessage {
    private String statusCode;
    private String errorMsg;
    private String description;
    private ZonedDateTime timestamp;

    public ErrorMessage(String statusCode, String errorMsg, String description, ZonedDateTime timestamp) {
        this.statusCode = statusCode;
        this.errorMsg = errorMsg;
        this.description = description;
        this.timestamp = timestamp;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
