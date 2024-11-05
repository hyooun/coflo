package com.reviewping.coflo.global.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

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

        String body =
                String.format(
                        "{\"text\": \"%s\"}", message.replace("\n", "\\n").replace("\"", "\\\""));

        log.info("sendWebHookMessage {}", body);

        try {
            RestTemplateUtils.sendPostRequest(
                    BASE_URL, headers, body, new ParameterizedTypeReference<String>() {});
            log.info("WebHook message sent successfully.");
        } catch (HttpClientErrorException e) {
            log.error(
                    "Failed to send WebHook message - Status code: {}, Response body: {}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(
                    "An unexpected error occurred while sending WebHook message: {}",
                    e.getMessage(),
                    e);
        }
    }
}
