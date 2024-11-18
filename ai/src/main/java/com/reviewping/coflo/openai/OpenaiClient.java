package com.reviewping.coflo.openai;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.openai.dto.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenaiClient {
    private static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String EMBEDDING_URL = "https://api.openai.com/v1/embeddings";

    private static final String GPT_4O_MINI = "gpt-4o-mini";
    private static final String TEXT_EMBEDDING_3_SMALL = "text-embedding-3-small";

    private static final String ROLE_USER = "user";

    private final RestTemplate restTemplate;
    private final JsonUtil jsonUtil;
    private final HttpHeaders headers;

    public OpenaiClient(
            RestTemplate restTemplate,
            JsonUtil jsonUtil,
            @Value("${openai.api.key}") String openaiApiKey,
            @Value("${openai.api.organization}") String openaiOrganization,
            @Value("${openai.api.project}") String openaiProject) {
        this.restTemplate = restTemplate;
        this.jsonUtil = jsonUtil;
        this.headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(AUTHORIZATION, "Bearer " + openaiApiKey);
        headers.set("OpenAI-Organization", openaiOrganization);
        headers.set("OpenAI-Project", openaiProject);
    }

    public EmbeddingResponse generateEmbedding(String content) {
        EmbeddingRequest embeddingRequest = new EmbeddingRequest(content, TEXT_EMBEDDING_3_SMALL);
        String body = jsonUtil.toJson(embeddingRequest);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<EmbeddingResponse> exchange =
                restTemplate.exchange(
                        EMBEDDING_URL, HttpMethod.POST, entity, EmbeddingResponse.class);
        return exchange.getBody();
    }

    public ChatCompletionResponse chat(String prompt) {

        ChatCompletionRequest chatCompletionRequest =
                new ChatCompletionRequest(GPT_4O_MINI, List.of(new ChatMessage(ROLE_USER, prompt)));
        String body = jsonUtil.toJson(chatCompletionRequest);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<ChatCompletionResponse> exchange =
                restTemplate.exchange(
                        CHAT_URL, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
        return exchange.getBody();
    }
}
