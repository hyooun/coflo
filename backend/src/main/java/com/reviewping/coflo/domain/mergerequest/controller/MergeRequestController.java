package com.reviewping.coflo.domain.mergerequest.controller;

import com.reviewping.coflo.domain.mergerequest.controller.dto.request.GitlabMrPageRequest;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrPageResponse;
import com.reviewping.coflo.domain.mergerequest.controller.dto.response.GitlabMrQueryResponse;
import com.reviewping.coflo.domain.mergerequest.service.MergeRequestService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merge-requests")
public class MergeRequestController {

    private final MergeRequestService mergeRequestService;

    @GetMapping
    @Operation(summary = "프로젝트의 merge request 목록 조회", description = "키워드 검색 가능, 상태 필터, 페이지네이션 제공")
    public ApiResponse<GitlabMrPageResponse> getGitlabMergeRequests(
            @AuthUser User user,
            @RequestParam(name = "projectId") Long projectId,
            @RequestParam(name = "state", defaultValue = "opened") String state,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        GitlabMrPageResponse gitlabMergeRequests =
                mergeRequestService.getGitlabMergeRequests(
                        user.getId(),
                        projectId,
                        new GitlabMrPageRequest(state, size, page, keyword));
        return ApiSuccessResponse.success(gitlabMergeRequests);
    }

    @GetMapping("/best")
    @Operation(summary = "프로젝트의 주간 Best MR 조회", description = "점수 가장 높은 상위 3개 항목 조회")
    public ApiResponse<List<GitlabMrQueryResponse>> getBestGitlabMergeRequests(
            @AuthUser User user, @RequestParam(name = "projectId") Long projectId) {
        List<GitlabMrQueryResponse> gitlabMrResponses =
                mergeRequestService.getBestMergeRequests(user.getId(), projectId);
        return ApiSuccessResponse.success(gitlabMrResponses);
    }
}
