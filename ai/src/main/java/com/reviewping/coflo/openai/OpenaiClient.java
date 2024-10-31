package com.reviewping.coflo.openai;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.reviewping.coflo.json.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenaiClient {
    // TODO
    private static final String EMBEDDING_URL = "https://api.openai.com/v1/embeddings";
    private static final String EMBEDDING_MODEL = "text-embedding-3-small";

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
        EmbeddingRequest embeddingRequest = new EmbeddingRequest(content, EMBEDDING_MODEL);
        String body = jsonUtil.toJson(embeddingRequest);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<EmbeddingResponse> exchange =
                restTemplate.exchange(
                        EMBEDDING_URL, HttpMethod.POST, entity, EmbeddingResponse.class);
        return exchange.getBody();
    }
}
