package com.tds.url_shortener;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<ShortenUrlResponseDto> shortenUrl(@RequestBody UrlRequestDto urlRequest,
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

}
