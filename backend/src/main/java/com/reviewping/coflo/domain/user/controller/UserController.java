package com.reviewping.coflo.domain.user.controller;

import com.reviewping.coflo.domain.user.controller.dto.request.GitlabAccountRequest;
import com.reviewping.coflo.domain.user.entity.PrincipalDetail;
import com.reviewping.coflo.domain.user.service.UserService;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/me")
    public ApiResponse<Void> addGitlabAccount(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestBody GitlabAccountRequest gitlabAccountRequest) {
        userService.addGitlabAccount(
                gitlabAccountRequest.domain(),
                gitlabAccountRequest.userToken(),
                principalDetail.getUsername());
        return ApiSuccessResponse.success();
    }

    @PatchMapping("/me/sync")
    public ApiResponse<Void> synchronizeUserInfo(
            @AuthenticationPrincipal PrincipalDetail principalDetail) {
        userService.synchronizeUserInfo(principalDetail.getUsername());
        return ApiSuccessResponse.success();
    }
}
