package com.reviewping.coflo.logging;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
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
    private PatternLayoutEncoder encoder;

    @Override
    public void start() {
        // PatternLayoutEncoder 초기화
        if (this.encoder != null) {
            this.encoder.setContext(getContext());
            this.encoder.start();
        }
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (!isStarted()) {
            return;
        }

        // encoder를 사용해 포맷팅된 메시지 생성
        String formattedMessage =
                this.encoder != null
                        ? this.encoder.getLayout().doLayout(eventObject)
                        : eventObject.getFormattedMessage();

        Map<String, String> payload = new HashMap<>();
        payload.put("text", formattedMessage);
        String body = jsonUtil.toJson(payload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, request, String.class);
    }
}
