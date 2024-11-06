package com.reviewping.coflo.service;

import static com.reviewping.coflo.service.dto.request.ReviewRequestMessage.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.entity.ChunkedCode;
import com.reviewping.coflo.gateway.RedisGateway;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.ChatCompletionResponse;
import com.reviewping.coflo.openai.dto.EmbeddingResponse;
import com.reviewping.coflo.repository.PromptTemplateRepository;
import com.reviewping.coflo.repository.VectorRepository;
import com.reviewping.coflo.service.dto.request.ReviewRegenerateRequestMessage;
import com.reviewping.coflo.service.dto.request.ReviewRequestMessage;
import com.reviewping.coflo.service.dto.response.RetrievalMessage;
import com.reviewping.coflo.service.dto.response.ReviewResponseMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCreateService {

    private static final String PROMPT_TYPE = "REVIEW";

    private final RedisGateway redisGateway;
    private final OpenaiClient openaiClient;
    private final JsonUtil jsonUtil;
    private final VectorRepository vectorRepository;
    private final PromptTemplateRepository promptTemplateRepository;

    @ServiceActivator(inputChannel = "reviewRequestChannel")
    public void createReview(String reviewRequestMessage) {
        ReviewRequestMessage reviewRequest =
                jsonUtil.fromJson(reviewRequestMessage, new TypeReference<>() {});
        Long projectId = reviewRequest.projectId();
        Long branchId = reviewRequest.branchId();
        // 1. mr 임베딩
        EmbeddingResponse embeddingResponse =
                openaiClient.generateEmbedding(reviewRequest.mrContent().mrDiffs());
        float[] embedding = embeddingResponse.data().getFirst().embedding();
        // 2. 참고자료 검색
        List<ChunkedCode> chunkedCodes =
                vectorRepository.retrieveRelevantData(projectId, branchId, 10, embedding);
        List<RetrievalMessage> retrievals =
                chunkedCodes.stream().map(RetrievalMessage::from).toList();
        // 3. 프롬프트 생성
        String prompt =
                buildPrompt(reviewRequest.mrContent(), reviewRequest.customPrompt(), retrievals);
        // 4. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage = chatCompletionResponse.choices().getFirst().message().content();
        // 5. 리뷰 생성 완료
        ReviewResponseMessage reviewResponse =
                new ReviewResponseMessage(
                        reviewRequest.gitlabUrl(),
                        reviewRequest.mrInfoId(),
                        chatMessage,
                        retrievals);
        redisGateway.sendReview(reviewResponse);
    }

    @ServiceActivator(inputChannel = "reviewRegenerateRequestChannel")
    public void regenerateReview(String reviewRegenerateRequestMessage) {
        ReviewRegenerateRequestMessage reviewRequest =
                jsonUtil.fromJson(reviewRegenerateRequestMessage, new TypeReference<>() {});
        // 1. 프롬프트 생성
        String prompt =
                buildPrompt(
                        reviewRequest.mrContent(),
                        reviewRequest.customPrompt(),
                        reviewRequest.retrievals());
        // 2. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage = chatCompletionResponse.choices().getFirst().message().content();
        // 3. 리뷰 생성 완료
        ReviewResponseMessage reviewResponse =
                new ReviewResponseMessage(
                        reviewRequest.gitlabUrl(),
                        reviewRequest.mrInfoId(),
                        chatMessage,
                        reviewRequest.retrievals());
        redisGateway.sendReview(reviewResponse);
    }

    private String buildPrompt(
            MrContent mrContent, String customPrompt, List<RetrievalMessage> retrievals) {
        String prompt = promptTemplateRepository.findLatestTemplate(PROMPT_TYPE).content();
        return prompt.formatted(
                retrievals, mrContent.mrDescription(), mrContent.mrDiffs(), customPrompt);
    }
}
