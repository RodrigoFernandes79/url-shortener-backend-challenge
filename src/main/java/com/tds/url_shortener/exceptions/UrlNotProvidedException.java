package com.tds.url_shortener.exceptions;

public class UrlNotProvidedException extends RuntimeException {
    public UrlNotProvidedException(String message) {
        super(message);
    }
}
