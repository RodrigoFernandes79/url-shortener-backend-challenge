package com.tds.url_shortener.service;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.domain.entity.UrlEntity;
import com.tds.url_shortener.exceptions.OriginalUrlFoundException;
import com.tds.url_shortener.exceptions.UrlIdNotFoundException;
import com.tds.url_shortener.repository.UrlEntityRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {
    @InjectMocks
    private UrlService urlService;
    @Mock
    private UrlEntityRepository urlRepository;
    private UrlRequestDto urlRequest;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Captor
    private ArgumentCaptor<UrlEntity> argumentCaptorUrlEntity;


    @Test
    @DisplayName("Should throws an exception when original url is present")
    void shortenUrlScenario01() {
        //Arrange
        this.urlRequest = new UrlRequestDto("https://www.original_url.com");
        given(urlRepository.findByOriginalUrl(urlRequest.originalUrl())).willReturn(Optional.of(new UrlEntity()));

        //Act & Assertions
        var exception = Assertions.assertThrows(OriginalUrlFoundException.class, () -> urlService.shortenUrl(urlRequest, httpServletRequest));
        assertEquals("Original Url Already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Should save a entity when validates pass")
    void shortenUrlScenario02() {
        // Arrange
        this.urlRequest = new UrlRequestDto("https://www.original_url.com");

        given(urlRepository.findByOriginalUrl(urlRequest.originalUrl())).willReturn(Optional.empty());
        given(httpServletRequest.getRequestURL()).willReturn(new StringBuffer("http://localhost:8080/api/v1/urls/shorten"));
        // Act
        ShortenUrlResponseDto shortenedUrlResponseDto = urlService.shortenUrl(urlRequest, httpServletRequest);
        // Assertions
        then(urlRepository).should().save(argumentCaptorUrlEntity.capture());
        UrlEntity savedUrlEntity = argumentCaptorUrlEntity.getValue();

        assertAll(
                () -> assertEquals(savedUrlEntity.getShortUrl(), shortenedUrlResponseDto.shortUrl()),
                () -> assertEquals(savedUrlEntity.getOriginalUrl(), urlRequest.originalUrl()),
                () -> assertTrue(shortenedUrlResponseDto.shortUrl().contains("http://localhost:8080/api/v1/urls/"))
        );
    }

    @Test
    @DisplayName("Should throws an exception when Url id was not found into redirectUrl method")
    void redirectToUrlScenario01() {
        //Arrange
        String id = "abc123";
        given(urlRepository.findById(id)).willReturn(Optional.empty());

        //Act & Assertions
        var exception = assertThrows(UrlIdNotFoundException.class, () -> urlService.redirectToUrl(id));
        assertEquals("Url id Not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should save the accessCount and the LastAccessed when will find urlEntity")
    void redirectToUrlScenario02() {
        //Arrange
        String id = "abc123";
        given(urlRepository.findById(id)).willReturn(Optional.of(new UrlEntity()));
        //Act
        var urlRequestDto = urlService.redirectToUrl(id);

        //Assertions
        then(urlRepository).should().save(argumentCaptorUrlEntity.capture());
        var savedUrlEntity = argumentCaptorUrlEntity.getValue();

        assertAll(
                () -> assertEquals(savedUrlEntity.getOriginalUrl(), urlRequestDto.originalUrl()),
                () -> assertEquals(1, savedUrlEntity.getAccessCount()),
                () -> assertTrue(savedUrlEntity.getLastAccessed().toLocalDate().isEqual(LocalDate.now()))
        );
    }

    @Test
    @DisplayName("Should calculate access average per day when there is more than one day")
    void getUrlStatisticsScenario01() {
        //Arrange
        String id = "abc123";
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setAccessCount(20);
        urlEntity.setCreatedAt(LocalDateTime.now().minusDays(10));
        urlEntity.setLastAccessed(LocalDateTime.now());
        long daysSinceCreation = Duration.between(urlEntity.getCreatedAt(), urlEntity.getLastAccessed()).toDays();

        double averageAccessesPerDay = daysSinceCreation > 0
                ? (double) urlEntity.getAccessCount() / daysSinceCreation
                : (double) urlEntity.getAccessCount();

        given(urlRepository.findById(id)).willReturn(Optional.of(urlEntity));

        // Act
        var urlStatistics = urlService.getUrlStatistics(id);

        // Assert
        assertAll(
                () -> assertEquals(2.0, urlStatistics.averageAccessesPerDay()),
                () -> assertEquals(20, urlStatistics.totalAccess()),
                () -> assertEquals(averageAccessesPerDay, urlStatistics.averageAccessesPerDay()),
                () -> assertEquals(urlEntity.getAccessCount(), urlStatistics.totalAccess())
        );
    }

    @Test
    @DisplayName("Should return the total access count when the number of days is zero")
    void getUrlStatisticsScenario02() {
        //Arrange
        String id = "abc123";
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setAccessCount(15);
        urlEntity.setCreatedAt(LocalDateTime.now());
        urlEntity.setLastAccessed(LocalDateTime.now());
        long daysSinceCreation = Duration.between(urlEntity.getCreatedAt(), urlEntity.getLastAccessed()).toDays();

        double averageAccessesPerDay = daysSinceCreation > 0
                ? (double) urlEntity.getAccessCount() / daysSinceCreation
                : (double) urlEntity.getAccessCount();

        given(urlRepository.findById(id)).willReturn(Optional.of(urlEntity));

        // Act
        var urlStatistics = urlService.getUrlStatistics(id);

        // Assert
        assertAll(
                () -> assertEquals(15, urlStatistics.averageAccessesPerDay()),
                () -> assertEquals(15, urlStatistics.totalAccess()),
                () -> assertEquals(averageAccessesPerDay, urlStatistics.averageAccessesPerDay()),
                () -> assertEquals(urlEntity.getAccessCount(), urlStatistics.totalAccess())
        );
    }
}