package com.reviewping.coflo.global.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebHookUtils {

    private static String BASE_URL;

    @Value("${mattermost-logger.base-url}")
    private String baseUrlProperty;

    @PostConstruct
    private void init() {
        BASE_URL = baseUrlProperty;
        log.info("WebHookUtils initialized with BASE_URL: {}", BASE_URL);
    }

    public static void sendWebHookMessage(String message) {
        String CONTENT_TYPE = "application/json";
        HttpHeaders headers = RestTemplateUtils.createHeaders(CONTENT_TYPE);

        String body = "{\"text\":\"" + message + "\"}";

        log.info("sendWebHookMessage {}", body);

        RestTemplateUtils.sendPostRequest(
                BASE_URL, headers, body, new ParameterizedTypeReference<>() {});
    }
}
