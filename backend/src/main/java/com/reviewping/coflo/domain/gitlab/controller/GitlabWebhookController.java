package com.reviewping.coflo.domain.gitlab.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.service.MrEventHandler;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequiredArgsConstructor
public class GitlabWebhookController {

    private static final String MERGE_REQUEST_TYPE = "Merge Request Hook";

    private final MrEventHandler mrEventHandler;
    private final ObjectMapper objectMapper;

    @PostMapping("/webhook/{projectId}")
    public ApiResponse<Void> handleGitlabEvent(
            @RequestHeader("X-Gitlab-Event") String gitlabEventType,
            @PathVariable("projectId") Long projectId,
            @RequestBody String requestBody) {
        if (!gitlabEventType.equals(MERGE_REQUEST_TYPE)) {
            throw new BusinessException(ErrorCode.UNSUPPORTED_EVENT_TYPE);
        }

        GitlabEventRequest gitlabEventRequest;
        try {
            gitlabEventRequest = objectMapper.readValue(requestBody, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.GITLAB_EVENT_REQUEST_SERIALIZATION_ERROR, e);
        }
        mrEventHandler.handle(projectId, gitlabEventRequest);
        return ApiSuccessResponse.success();
    }
}
