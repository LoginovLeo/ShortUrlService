package com.example.shorturl.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MissingParamUrl.class)
    public ResponseEntity<?> missingParamUrl(MissingParamUrl ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage(),
                request.getDescription(false),
                ZonedDateTime.now());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                request.getDescription(false),
                ZonedDateTime.now());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpRequestMethodNotSupportedException ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                HttpStatus.METHOD_NOT_ALLOWED.toString(),
                ex.getMessage().concat(". Supported methods ").concat(Objects.requireNonNull(ex.getSupportedHttpMethods()).toString()),
                request.getDescription(false),
                ZonedDateTime.now());

        return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
    }



}
