package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.gateway.RedisGateway;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.ChatCompletionResponse;
import com.reviewping.coflo.service.dto.MrContent;
import com.reviewping.coflo.service.dto.ReviewRequest;
import com.reviewping.coflo.service.dto.ReviewResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewCreateService {

    private final RedisGateway redisGateway;
    private final OpenaiClient openaiClient;
    private final JsonUtil jsonUtil;

    @ServiceActivator(inputChannel = "reviewRequestChannel")
    public void createReview(String reviewRequestMessage) {
        ReviewRequest reviewRequest =
                jsonUtil.fromJson(reviewRequestMessage, new TypeReference<>() {});
        // 1. TODO: mr 임베딩
        // 2. TODO: 참고자료 검색
        // 3. 프롬프트 생성
        String prompt = buildPrompt(reviewRequest.mrContent(), reviewRequest.customPrompt());
        // 4. 리뷰 생성
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(prompt);
        String chatMessage = chatCompletionResponse.choices().getFirst().message().content();
        // 5. 리뷰 생성 완료
        ReviewResponse reviewResponse =
                new ReviewResponse(
                        reviewRequest.gitlabUrl(),
                        reviewRequest.mrInfoId(),
                        chatMessage,
                        List.of());
        redisGateway.sendReview(reviewResponse);
    }

    // TODO: 프롬프트 개선
    private String buildPrompt(MrContent mrContent, String customPrompt) {
        return mrContent.mrDescription() + mrContent.mrDiffs() + customPrompt;
    }
}
