package com.loginov.shorturl.exeption;

import com.loginov.shorturl.exeption.customexceptions.CustomBadRequestException;
import com.loginov.shorturl.exeption.customexceptions.CustomNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZonedDateTime;
import java.util.Objects;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleCustomNotFoundException(CustomNotFoundException ex, WebRequest request) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.toString(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> handleCustomBadRequestException(CustomBadRequestException ex, WebRequest request) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.toString(),
                ex.getParameterName().concat(" parameter is missing"),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        LOGGER.error(HttpStatus.BAD_REQUEST + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.toString(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        LOGGER.error(HttpStatus.METHOD_NOT_ALLOWED + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.METHOD_NOT_ALLOWED.toString(),
                ex.getMessage().concat(". Supported methods ").concat(Objects.requireNonNull(ex.getSupportedHttpMethods()).toString()),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
        LOGGER.error(HttpStatus.UNSUPPORTED_MEDIA_TYPE + " " + ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
                ex.getMessage().concat(". Supported types ").concat(Objects.requireNonNull(ex.getSupportedMediaTypes()).toString()),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalHandler(Exception ex, WebRequest request) {

        ErrorMessage message = new ErrorMessage(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
