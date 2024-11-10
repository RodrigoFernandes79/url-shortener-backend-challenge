package com.tds.url_shortener.domain.dto;

public record UrlStatisticsDto(
        int totalAccess,
        double averageAccessesPerDay
) {
}
