package com.reviewping.coflo.domain.user.controller;

import com.reviewping.coflo.domain.user.controller.dto.request.GitlabAccountRequest;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.service.UserService;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
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
            @AuthUser User user, @RequestBody GitlabAccountRequest gitlabAccountRequest) {
        userService.addGitlabAccount(
                gitlabAccountRequest.domain(), gitlabAccountRequest.userToken(), user.getId());
        return ApiSuccessResponse.success();
    }

    @PatchMapping("/me/sync")
    public ApiResponse<Void> synchronizeUserInfo(@AuthUser User user) {
        userService.synchronizeUserInfo(user.getId());
        return ApiSuccessResponse.success();
    }
}
