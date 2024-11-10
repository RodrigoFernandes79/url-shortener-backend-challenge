package com.tds.url_shortener.service;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.domain.entity.UrlEntity;
import com.tds.url_shortener.exceptions.OriginalUrlFoundException;
import com.tds.url_shortener.exceptions.UrlIdNotFoundException;
import com.tds.url_shortener.exceptions.UrlNotProvidedException;
import com.tds.url_shortener.repository.UrlEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlService {
    @Autowired
    private UrlEntityRepository urlRepository;

    public ShortenUrlResponseDto shortenUrl(UrlRequestDto urlRequest, HttpServletRequest httpServletRequest) {
        if (urlRequest.originalUrl() == null || urlRequest.originalUrl().trim().isEmpty()) {
            throw new UrlNotProvidedException("Original Url is not provided");
        }
        var urlFound = urlRepository.findByOriginalUrl(urlRequest.originalUrl());
        if (urlFound.isPresent()) {
            throw new OriginalUrlFoundException("Original Url Already exists");
        }
        String id;
        do {
            id = RandomStringUtils.randomAlphanumeric(5, 10);
        } while (urlRepository.existsById(id));
        var redirectUrl = httpServletRequest.getRequestURL().toString().replace("shorten", id);
        urlRepository.save(new UrlEntity(id, urlRequest.originalUrl(), redirectUrl));
        return new ShortenUrlResponseDto(redirectUrl);
    }

    public UrlRequestDto redirectToUrl(String id) {
        var url = urlRepository.findById(id);
        if (url.isEmpty()) {
            throw new UrlIdNotFoundException("Url id Not found");
        }
        var urlFound = url.get();
        urlFound.setAccessCount(urlFound.getAccessCount() + 1);
        urlFound.setLastAccessed(LocalDateTime.now());

        urlRepository.save(urlFound);
        return new UrlRequestDto(urlFound.getOriginalUrl());
    }
}
