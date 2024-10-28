package com.reviewping.coflo.domain.mergerequest.controller;

import com.reviewping.coflo.domain.link.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.service.MergeRequestService;
import com.reviewping.coflo.global.auth.oauth.model.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merge-requests")
public class MergeRequestController {

    private final MergeRequestService mergeRequestService;

    @GetMapping
    public ApiResponse<GitlabMrPageResponse> getGitlabMergeRequests(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam(name = "project_id") Long projectId,
            @RequestParam(name = "state") String state,
            @RequestBody GitlabSearchRequest gitlabSearchRequest) {
        GitlabMrPageResponse gitlabMergeRequests =
                mergeRequestService.getGitlabMergeRequests(
                        authUser.getUserId(), projectId, state, gitlabSearchRequest);
        return ApiSuccessResponse.success(gitlabMergeRequests);
    }
}
