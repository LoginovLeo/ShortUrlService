package com.loginov.shorturl.exeption;

public class MissingParamUrl extends RuntimeException{

    private String message;


    public MissingParamUrl(String message) {
        super(message);
        this.message = message;
    }


}
