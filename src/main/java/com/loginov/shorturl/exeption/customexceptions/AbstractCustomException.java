package com.loginov.shorturl.exeption.customexceptions;

public abstract class AbstractCustomException extends RuntimeException {
    private String message;


    public AbstractCustomException(String message) {
        super(message);
        this.message = message;
    }
}
