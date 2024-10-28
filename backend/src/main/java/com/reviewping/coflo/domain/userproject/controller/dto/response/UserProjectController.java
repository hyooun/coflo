package com.reviewping.coflo.domain.userproject.controller.dto.response;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.service.UserProjectService;
import com.reviewping.coflo.global.auth.oauth.model.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam("currentProjectId") Long currentProjectId) {
        List<UserProjectResponse> userProjects =
                userProjectService.getUserProjects(User.builder().build(), currentProjectId);
        return ApiSuccessResponse.success(userProjects);
    }
}
