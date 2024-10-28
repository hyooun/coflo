package com.reviewping.coflo.global.util;

import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateUtils {

    static final String HEADER_CONTENT_TYPE = "Content-Type";
    static final String HEADER_AUTHORIZATION = "Authorization";
    static final String AUTH_TYPE = "Bearer ";

    private static final RestTemplate restTemplate = new RestTemplate();

    public static HttpHeaders createHeaders(String contentType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_CONTENT_TYPE, contentType);
        headers.set(HEADER_AUTHORIZATION, AUTH_TYPE + token);
        return headers;
    }

    public static <T> ResponseEntity<T> sendGetRequest(
            String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
        } catch (HttpClientErrorException e) {
            throw handleClientOrServerError(e);
        }
    }

    public static <T> ResponseEntity<T> sendPostRequest(
            String url,
            HttpHeaders headers,
            String body,
            ParameterizedTypeReference<T> responseType) {
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        } catch (HttpClientErrorException e) {
            throw handleClientOrServerError(e);
        }
    }

    private static BusinessException handleClientOrServerError(HttpClientErrorException e) {
        if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)
                || e.getStatusCode().equals(HttpStatus.FORBIDDEN)) {
            return new BusinessException(ErrorCode.EXTERNAL_API_UNAUTHORIZED);
        } else if (e.getStatusCode().is4xxClientError()) {
            return new BusinessException(ErrorCode.EXTERNAL_API_BAD_REQUEST);
        } else if (e.getStatusCode().is5xxServerError()) {
            return new BusinessException(ErrorCode.EXTERNAL_API_INTERNAL_SERVER_ERROR);
        }
        return new BusinessException(ErrorCode.EXTERNAL_API_INTERNAL_SERVER_ERROR);
    }
}
