package com.loginov.shorturl.exeption;

import java.time.ZonedDateTime;

public class ErrorMessage {
    private ZonedDateTime timestamp;
    private String status;
    private String errorMsg;
    private String description;

    public ErrorMessage(ZonedDateTime timestamp, String status, String errorMsg, String description) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorMsg = errorMsg;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
