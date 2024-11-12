package com.reviewping.coflo.domain.user.controller;

import com.reviewping.coflo.domain.user.controller.dto.request.GitlabAccountRequest;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.service.UserService;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/me")
    @Operation(summary = "현재 로그인 된 사용자의 연동된 Gitlab 정보 확인")
    public ApiResponse<Void> addGitlabAccount(
            @AuthUser User user, @Valid @RequestBody GitlabAccountRequest gitlabAccountRequest) {
        userService.addGitlabAccount(
                gitlabAccountRequest.domain(), gitlabAccountRequest.userToken(), user.getId());
        return ApiSuccessResponse.success();
    }

    @PatchMapping("/me/sync")
    @Operation(summary = "사용자 정보 동기화", description = "username과 avatar_url 동기화")
    public ApiResponse<Void> synchronizeUserInfo(@AuthUser User user) {
        userService.synchronizeUserInfo(user.getId());
        return ApiSuccessResponse.success();
    }

    @PatchMapping("/recent-project/{projectId}")
    @Operation(summary = "최근 조회한 프로젝트 식별자", description = "GitlabAccount의 recentProjectId 변경")
    public ApiResponse<Void> updateRecentProject(
            @AuthUser User user, @PathVariable Long projectId) {
        userService.updateRecentProjectId(user.getId(), projectId);
        return ApiSuccessResponse.success();
    }
}
