package com.tds.url_shortener.controller;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.domain.dto.UrlStatisticsDto;
import com.tds.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/shorten")
    @Transactional
    public ResponseEntity<ShortenUrlResponseDto> shortenUrl(@Valid @RequestBody UrlRequestDto urlRequest,
                                                            HttpServletRequest httpServletRequest) {
        var shortenUrlResponse = urlService.shortenUrl(urlRequest, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortenUrlResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToUrl(@PathVariable String id) {
        var originalUrl = urlService.redirectToUrl(id);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl.originalUrl()))
                .build();
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<UrlStatisticsDto> getUrlStatistics(@PathVariable String id) {
        var statistics = urlService.getUrlStatistics(id);
        return ResponseEntity.ok().body(statistics);
    }

}
