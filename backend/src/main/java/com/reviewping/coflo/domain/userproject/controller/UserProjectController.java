package com.reviewping.coflo.domain.userproject.controller;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.controller.dto.response.UserProjectResponse;
import com.reviewping.coflo.domain.userproject.service.UserProjectService;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/project")
public class UserProjectController {

    private final UserProjectService userProjectService;

    @GetMapping
    public ApiResponse<List<UserProjectResponse>> getUserProjects(
            @AuthUser User user,
            @RequestParam(name = "currentProjectId", defaultValue = "-1") Long currentProjectId) {
        List<UserProjectResponse> userProjects =
                userProjectService.getUserProjects(user, currentProjectId);
        return ApiSuccessResponse.success(userProjects);
    }
}
