package com.tds.url_shortener.exceptions;

public class UrlIdNotFoundException extends RuntimeException {
    public UrlIdNotFoundException(String message) {
        super(message);
    }
}