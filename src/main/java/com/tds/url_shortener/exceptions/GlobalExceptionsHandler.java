package com.tds.url_shortener.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionsHandler {
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataFieldValidation>> dataField(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        var error = errors.stream().map(DataFieldValidation::new).toList();
        return ResponseEntity.badRequest().body(error);
    }

    private record DataFieldValidation(String error, String message) {
        private DataFieldValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
