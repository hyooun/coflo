package com.reviewping.coflo.domain.gitlab.controller;

import com.reviewping.coflo.domain.gitlab.controller.dto.request.BotTokenValidateRequest;
import com.reviewping.coflo.domain.gitlab.controller.dto.request.GitlabSearchRequest;
import com.reviewping.coflo.domain.gitlab.controller.dto.response.GitlabProjectPageResponse;
import com.reviewping.coflo.domain.gitlab.service.GitlabApiService;
import com.reviewping.coflo.domain.user.controller.dto.request.GitlabAccountRequest;
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
@RequestMapping("/api/gitlab")
@RequiredArgsConstructor
public class GitlabApiController {

    private final GitlabApiService gitlabApiService;

    @GetMapping("/search")
    @Operation(summary = "사용자의 Gitlab repository 조회", description = "키워드 검색 가능, 페이지네이션 기능 제공")
    public ApiResponse<GitlabProjectPageResponse> getGitlabProjects(
            @AuthUser User user,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        GitlabProjectPageResponse gitlabProjects =
                gitlabApiService.getGitlabProjects(
                        user.getId(), new GitlabSearchRequest(keyword, page, size));
        return ApiSuccessResponse.success(gitlabProjects);
    }

    @GetMapping("/{gitlabProjectId}/branches")
    @Operation(summary = "프로젝트 연동 시 branch 목록 조회")
    public ApiResponse<List<String>> getGitlabProjectBranches(
            @AuthUser User user, @PathVariable("gitlabProjectId") Long gitlabProjectId) {
        return ApiSuccessResponse.success(
                gitlabApiService.getGitlabProjectBranches(user.getId(), gitlabProjectId));
    }

    @PostMapping("/user-token/validate")
    @Operation(summary = "User Token 유효성 검증")
    public ApiResponse<Boolean> validateUserToken(
            @RequestBody GitlabAccountRequest gitlabAccountRequest) {
        return ApiSuccessResponse.success(
                gitlabApiService.validateUserToken(
                        gitlabAccountRequest.domain(), gitlabAccountRequest.userToken()));
    }

    @PostMapping("/bot-token/validate")
    @Operation(summary = "Bot Token 유효성 검증")
    public ApiResponse<Boolean> validateUserToken(
            @AuthUser User user, @RequestBody BotTokenValidateRequest botTokenValidateRequest) {
        return ApiSuccessResponse.success(
                gitlabApiService.validateBotToken(
                        user.getId(),
                        botTokenValidateRequest.gitlabProjectId(),
                        botTokenValidateRequest.botToken()));
    }
}
