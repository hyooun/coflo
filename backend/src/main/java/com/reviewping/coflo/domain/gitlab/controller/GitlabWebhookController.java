package com.reviewping.coflo.domain.gitlab.controller;

import com.reviewping.coflo.domain.gitlab.service.GitlabEventHandler;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.client.gitlab.request.GitlabEventRequest;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequiredArgsConstructor
public class GitlabWebhookController {

    private final GitlabEventHandler gitlabEventHandler;

    @PostMapping(value = "/webhook/{projectId}", headers = "X-Gitlab-Event=Merge Request Hook")
    @Operation(summary = "Gitlab webhook 등록 시 사용")
    public ApiResponse<Void> handleGitlabEvent(
            @PathVariable("projectId") Long projectId,
            @RequestBody GitlabEventRequest gitlabEventRequest) {
        gitlabEventHandler.handleMergeRequest(projectId, gitlabEventRequest);
        return ApiSuccessResponse.success();
    }
}
