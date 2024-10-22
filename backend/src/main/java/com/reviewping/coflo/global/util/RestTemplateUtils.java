package com.reviewping.coflo.global.util;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.reviewping.coflo.global.error.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateUtils {

	static final String CONTENT_TYPE = "Content-Type";
	static final String AUTHORIZATION = "Authorization";
	static final String BEARER = "Bearer ";

	private static final RestTemplate restTemplate = new RestTemplate();

	public static HttpHeaders createHeaders(String contentType, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(CONTENT_TYPE, contentType);
		headers.set(AUTHORIZATION, BEARER + token);
		return headers;
	}

	public static <T> ResponseEntity<T> sendGetRequest(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType) {
		HttpEntity<String> entity = new HttpEntity<>(headers);
		try {
			return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().is4xxClientError()) {
				return ResponseEntity.status(e.getStatusCode()).build();
			} else {
				throw new BusinessException(EXTERNAL_API_BAD_REQUEST);
			}
		}
	}

	public static <T> ResponseEntity<T> sendPostRequest(String url, HttpHeaders headers, String body,
		ParameterizedTypeReference<T> responseType) {
		HttpEntity<String> entity = new HttpEntity<>(body, headers);
		try {
			return restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
		} catch (HttpClientErrorException e) {
			throw new BusinessException(EXTERNAL_API_BAD_REQUEST);
		}
	}
}
