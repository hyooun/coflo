package com.reviewping.coflo.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reviewping.coflo.domain.user.dto.request.GitlabAccountRequest;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.service.UserService;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/me")
	public ApiResponse<Void> addGitlabAccount(
		@AuthenticationPrincipal User user,
		GitlabAccountRequest gitlabAccountRequest) {
		userService.addGitlabAccount(gitlabAccountRequest.domain(),
			gitlabAccountRequest.userToken(), user.getOauth2Id());
		return ApiSuccessResponse.success();
	}
}
