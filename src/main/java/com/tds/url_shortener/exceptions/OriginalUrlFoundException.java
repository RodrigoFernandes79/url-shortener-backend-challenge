package com.tds.url_shortener.exceptions;

public class OriginalUrlFoundException extends RuntimeException {
    public OriginalUrlFoundException(String message) {
        super(message);
    }
}