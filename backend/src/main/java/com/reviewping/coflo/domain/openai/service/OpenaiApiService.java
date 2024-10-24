package com.reviewping.coflo.domain.openai.service;

import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionRequest;
import com.reviewping.coflo.domain.openai.dto.response.ChatMessage;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenaiApiService {

    private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String openaiApiKey;

    public ChatCompletionContent chat(String prompt) {
        HttpHeaders headers = RestTemplateUtils.createHeaders("application/json", openaiApiKey);

        ChatCompletionRequest body =
                new ChatCompletionRequest("gpt-4o", List.of(new ChatMessage("user", prompt, null)));
        ResponseEntity<ChatCompletionContent> response =
                RestTemplateUtils.sendPostRequest(
                        CHAT_URL, headers, body.toString(), new ParameterizedTypeReference<>() {});
        return response.getBody();
    }
}
