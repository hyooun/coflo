package com.reviewping.coflo.domain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionContent;
import com.reviewping.coflo.domain.openai.dto.response.ChatCompletionRequest;
import com.reviewping.coflo.domain.openai.dto.response.ChatMessage;
import com.reviewping.coflo.global.util.RestTemplateUtils;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OpenaiApiService {

    private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private final ObjectMapper objectMapper;
    private final String openaiApiKey;
    private final String openaiOrganization;
    private final String openaiProject;

    public OpenaiApiService(
            ObjectMapper objectMapper,
            @Value("${openai.api.key}") String openaiApiKey,
            @Value("${openai.api.organization}") String openaiOrganization,
            @Value("${openai.api.project}") String openaiProject) {
        this.objectMapper = objectMapper;
        this.openaiApiKey = openaiApiKey;
        this.openaiOrganization = openaiOrganization;
        this.openaiProject = openaiProject;
    }

    public ChatCompletionContent chat(String prompt) {
        HttpHeaders headers = RestTemplateUtils.createHeaders("application/json", openaiApiKey);
        headers.set("OpenAI-Organization", openaiOrganization);
        headers.set("OpenAI-Project", openaiProject);

        ChatCompletionRequest chatCompletionRequest =
                new ChatCompletionRequest("gpt-4o-mini", List.of(new ChatMessage("user", prompt)));
        String body;
        try {
            body = objectMapper.writeValueAsString(chatCompletionRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<ChatCompletionContent> response =
                RestTemplateUtils.sendPostRequest(
                        CHAT_URL, headers, body, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }
}
