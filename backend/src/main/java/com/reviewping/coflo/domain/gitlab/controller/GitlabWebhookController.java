package com.reviewping.coflo.domain.gitlab.controller;

import static com.reviewping.coflo.global.error.ErrorCode.UNSUPPORTED_EVENT_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.gitlab.service.MrEventHandler;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiErrorResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Gitlab webhook 등록 시 사용")
    public ApiResponse<Void> handleGitlabEvent(
            @RequestHeader("X-Gitlab-Event") String gitlabEventType,
            @PathVariable("projectId") Long projectId,
            @RequestBody String requestBody) {
        if (!gitlabEventType.equals(MERGE_REQUEST_TYPE)) {
            return ApiErrorResponse.error(UNSUPPORTED_EVENT_TYPE);
        }

        GitlabEventRequest gitlabEventRequest;
        try {
            gitlabEventRequest = objectMapper.readValue(requestBody, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.GITLAB_EVENT_REQUEST_SERIALIZATION_ERROR, e);
        }
        try {
            mrEventHandler.handle(projectId, gitlabEventRequest);
        } catch (BusinessException e) {
            return ApiErrorResponse.error(e.getErrorCode());
        }

        return ApiSuccessResponse.success();
    }
}
