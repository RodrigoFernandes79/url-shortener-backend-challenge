package com.tds.url_shortener.controller;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.domain.dto.UrlStatisticsDto;
import com.tds.url_shortener.exceptions.ResponseError;
import com.tds.url_shortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Shorten a URL",
            description = "Creates a shortened URL for the given original URL",
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(schema = @Schema(implementation = ShortenUrlResponseDto.class))),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(schema = @Schema(implementation = ResponseError.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content)
            }
    )
    @PostMapping("/shorten")
    @Transactional
    public ResponseEntity<ShortenUrlResponseDto> shortenUrl(@Valid @RequestBody UrlRequestDto urlRequest,
                                                            HttpServletRequest httpServletRequest) {
        var shortenUrlResponse = urlService.shortenUrl(urlRequest, httpServletRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(shortenUrlResponse);
    }

    @Operation(
            summary = "Redirect to Original URL",
            description = "Redirects to the original URL associated with the given short URL ID. "
                    + "Due to CORS restrictions, this endpoint may not work as expected in the Swagger UI. "
                    + "For testing, please access it directly in your browser.",
            responses = {
                    @ApiResponse(description = "Found", responseCode = "302", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content)
            }
    )

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToUrl(@PathVariable String id) {
        var originalUrl = urlService.redirectToUrl(id);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl.originalUrl()))
                .build();

    }


    @Operation(
            summary = "Get URL Statistics",
            description = "Retrieve statistics for a shortened URL, including access count and average accesses per day",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UrlStatisticsDto.class))),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping("/{id}/statistics")
    public ResponseEntity<UrlStatisticsDto> getUrlStatistics(@PathVariable String id) {
        var statistics = urlService.getUrlStatistics(id);
        return ResponseEntity.ok().body(statistics);
    }

}

