package com.tds.url_shortener.domain.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record UrlRequestDto(
        @URL(message = "{url.format}")
        @NotBlank(message = "{url.required}")
        String originalUrl
) {
}
