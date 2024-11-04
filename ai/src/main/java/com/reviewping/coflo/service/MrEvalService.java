package com.reviewping.coflo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.reviewping.coflo.gateway.RedisGateway;
import com.reviewping.coflo.json.JsonUtil;
import com.reviewping.coflo.openai.OpenaiClient;
import com.reviewping.coflo.openai.dto.ChatCompletionResponse;
import com.reviewping.coflo.service.dto.request.MrEvalRequestMessage;
import com.reviewping.coflo.service.dto.request.ReviewRequestMessage.MrContent;
import com.reviewping.coflo.service.dto.response.MrEvalResponseMessage;
import com.reviewping.coflo.service.dto.response.MrEvalResponseMessage.MrEvaluationMessage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MrEvalService {

    private String EVAL_TEMPLATE;

    private final JsonUtil jsonUtil;
    private final OpenaiClient openaiClient;
    private final RedisGateway redisGateway;

    @PostConstruct
    public void setEvalTemplate() {
        MrEvaluationMessage mrEvaluationMessage = new MrEvaluationMessage(0, 0, 0, 0, 0, 0);
        EVAL_TEMPLATE = jsonUtil.toJson(mrEvaluationMessage);
    }

    @ServiceActivator(inputChannel = "mrEvalRequestChannel")
    public void evaluateMr(String mrEvalRequestMessage) {
        MrEvalRequestMessage evalRequest =
                jsonUtil.fromJson(mrEvalRequestMessage, new TypeReference<>() {});

        String evalPrompt = buildEvalPrompt(evalRequest.mrContent());
        ChatCompletionResponse chatCompletionResponse = openaiClient.chat(evalPrompt);
        MrEvaluationMessage mrEvaluationMessage =
                jsonUtil.fromJson(
                        chatCompletionResponse.choices().getFirst().message().content(),
                        new TypeReference<>() {});
        MrEvalResponseMessage mrEvalResponseMessage =
                new MrEvalResponseMessage(evalRequest.mrInfoId(), mrEvaluationMessage);
        redisGateway.sendEval(mrEvalResponseMessage);
    }

    private String buildEvalPrompt(MrContent mrContent) {
        return mrContent.mrDiffs()
                + "\n"
                + "이 MR 가독성, 일관성, 재사용성, 신뢰성, 보안성, 유지보수성에 대해서 평가한 후 **반드시 아래 json형식으로 반환해줘**\n"
                + "각 항목은 최대 10점이야."
                + "// template\n"
                + EVAL_TEMPLATE;
    }
}
