package com.tds.url_shortener.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(UrlNotProvidedException.class)
    public ResponseEntity<ResponseError> urlNotProvided(UrlNotProvidedException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OriginalUrlFoundException.class)
    public ResponseEntity<ResponseError> originalUrlFound(OriginalUrlFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UrlIdNotFoundException.class)
    public ResponseEntity<ResponseError> originalUrlFound(UrlIdNotFoundException ex) {
        ResponseError error = new ResponseError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
