package com.tds.url_shortener.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(
        String message,
        HttpStatus httpStatus,
        LocalDateTime time
) {
}