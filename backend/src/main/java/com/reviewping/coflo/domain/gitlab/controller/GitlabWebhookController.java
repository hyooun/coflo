package com.reviewping.coflo.domain.gitlab.controller;

import static com.reviewping.coflo.global.error.ErrorCode.UNSUPPORTED_EVENT_TYPE;

import com.reviewping.coflo.domain.gitlab.service.GitlabEventHandler;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiErrorResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import com.reviewping.coflo.global.error.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequiredArgsConstructor
public class GitlabWebhookController {

    private static final String MERGE_REQUEST_TYPE = "Merge Request Hook";
    private static final String PUSH_REQUEST_TYPE = "Push Hook";

    private final GitlabEventHandler gitlabEventHandler;

    @PostMapping("/webhook/{projectId}")
    @Operation(summary = "Gitlab webhook 등록 시 사용")
    public ApiResponse<Void> handleGitlabEvent(
            @RequestHeader("X-Gitlab-Event") String gitlabEventType,
            @PathVariable("projectId") Long projectId,
            @RequestBody GitlabEventRequest gitlabEventRequest) {
        return switch (gitlabEventType) {
            case MERGE_REQUEST_TYPE -> {
                try {
                    gitlabEventHandler.handleMergeRequest(projectId, gitlabEventRequest);
                } catch (BusinessException e) {
                    yield ApiErrorResponse.error(e.getErrorCode());
                }
                yield ApiSuccessResponse.success();
            }
            case PUSH_REQUEST_TYPE -> {
                gitlabEventHandler.handlePush(projectId, gitlabEventRequest);
                yield ApiSuccessResponse.success();
            }
            default -> ApiErrorResponse.error(UNSUPPORTED_EVENT_TYPE);
        };
    }
}
