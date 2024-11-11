package com.tds.url_shortener.controller;

import com.tds.url_shortener.domain.dto.ShortenUrlResponseDto;
import com.tds.url_shortener.domain.dto.UrlRequestDto;
import com.tds.url_shortener.domain.dto.UrlStatisticsDto;
import com.tds.url_shortener.exceptions.UrlIdNotFoundException;
import com.tds.url_shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UrlControllerTest {
    @MockBean
    private UrlService urlService;
    @Autowired
    private MockMvc mvc;
    private HttpServletRequest httpServletRequest;
    @Autowired
    private JacksonTester<UrlRequestDto> urlRequestDtoJacksonTester;
    @Autowired
    private JacksonTester<ShortenUrlResponseDto> urlResponseDtoJacksonTester;
    @Autowired
    private JacksonTester<UrlStatisticsDto> urlStatisticsDtoJacksonTester;


    @Test
    @DisplayName("Should return a 400 code to shortenUrl with error")
    void shortenUrlScenario01() throws Exception {
        //Arrange
        String json = "{}";
        //Act
        var response = mvc.perform(
                        post("/api/v1/urls/shorten")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //Assertions
        assertEquals(400, response.getStatus());

    }

    @Test
    @DisplayName("Should return status 201 when method shortenUrl will not get errors")
    void shortenUrlScenario02() throws Exception {
        //Arrange
        UrlRequestDto urlOriginal = new UrlRequestDto("https://www.original_url.com");
        ShortenUrlResponseDto shortenUrl = new ShortenUrlResponseDto("http://localhost:8080/api/v1/urls/shorten");

        when(urlService.shortenUrl(any(UrlRequestDto.class), any(HttpServletRequest.class))).thenReturn(shortenUrl);
        //Act
        var response = mvc.perform(
                        post("/api/v1/urls/shorten")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(urlRequestDtoJacksonTester
                                        .write(urlOriginal).getJson()
                                )
                ).andReturn()
                .getResponse();
        //Assertions
        var jsonResponse = response.getContentAsString();
        var jsonExpected = urlResponseDtoJacksonTester.write(shortenUrl).getJson();
        assertEquals(201, response.getStatus());
        assertEquals(jsonExpected, jsonResponse);

    }

    @Test
    @DisplayName("Should return a 404 status code when redirectToUrl will not find the id")
    void redirectToUrlScenario01() throws Exception {
        //Arrange
        String id = "abc123";
        when(urlService.redirectToUrl(id)).thenThrow(UrlIdNotFoundException.class);
        //Act
        var response = mvc.perform(
                        get("/api/v1/urls/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //Asserts
        assertEquals(404, response.getStatus(), "Url id Not found");
    }

    @Test
    @DisplayName("Should return a 302 status code when redirectToUrl will find the id")
    void redirectToUrlScenario02() throws Exception {
        //Arrange
        String id = "abc123";
        UrlRequestDto urlOriginal = new UrlRequestDto("https://www.original_url.com");

        when(urlService.redirectToUrl(id)).thenReturn(urlOriginal);
        //Act
        var response = mvc.perform(
                        get("/api/v1/urls/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //Asserts
        assertEquals(HttpStatus.FOUND.value(), response.getStatus());
        assertEquals(urlOriginal.originalUrl(), response.getRedirectedUrl());
    }

    @Test
    @DisplayName("Should return a 404 status code when getUrlStatistics will not find the id")
    void getUrlStatisticsScenario01() throws Exception {
        String id = "abc123";
        when(urlService.getUrlStatistics(id)).thenThrow(UrlIdNotFoundException.class);
        //Act
        var response = mvc.perform(
                        get("/api/v1/urls/{id}/statistics", id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //Asserts
        assertEquals(404, response.getStatus(), "Url id Not found");
    }

    @Test
    @DisplayName("Should return a 200 status code when getUrlStatistics will  find the id")
    void getUrlStatisticsScenario02() throws Exception {
        String id = "abc123";
        UrlStatisticsDto urlStatisticsDto = new UrlStatisticsDto(50, 10);
        when(urlService.getUrlStatistics(id)).thenReturn(urlStatisticsDto);
        //Act
        var response = mvc.perform(
                        get("/api/v1/urls/{id}/statistics", id)
                                .contentType(MediaType.APPLICATION_JSON)
                ).andReturn()
                .getResponse();
        //Asserts
        var jsonResponse = response.getContentAsString();
        var jsonExpected = urlStatisticsDtoJacksonTester.write(urlStatisticsDto).getJson();
        assertEquals(200, response.getStatus());
        assertEquals(jsonExpected, jsonResponse);
    }


}