package com.loginov.shorturl.exeption.customexceptions;

public class CustomBadRequestException extends AbstractCustomException{
    public CustomBadRequestException(String message) {
        super(message);
    }
}
