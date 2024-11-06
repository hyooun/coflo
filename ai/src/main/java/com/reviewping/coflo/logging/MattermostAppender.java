package com.reviewping.coflo.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.json.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Setter
public class MattermostAppender extends AppenderBase<ILoggingEvent> {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final JsonUtil jsonUtil = new JsonUtil(new ObjectMapper());

    private String url;

    @Override
    protected void append(ILoggingEvent eventObject) {
        Map<String, String> payload = new HashMap<>();
        payload.put("text", eventObject.getFormattedMessage());
        String body = jsonUtil.toJson(payload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}
