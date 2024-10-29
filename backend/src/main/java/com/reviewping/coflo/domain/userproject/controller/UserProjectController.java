package com.reviewping.coflo.domain.userproject.controller;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.domain.userproject.controller.dto.response.UserProjectResponse;
import com.reviewping.coflo.domain.userproject.service.UserProjectService;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-project")
public class UserProjectController {

    private final UserProjectService userProjectService;

    @PostMapping("/{gitlabProjectId}")
    public ApiResponse<Map<String, Long>> addUserProject(
            @AuthUser User user,
            @PathVariable("gitlabProjectId") Long gitlabProjectId,
            @RequestBody(required = false) ProjectLinkRequest projectLinkRequest) {
        Long projectId =
                userProjectService.linkGitlabProject(
                        user.getId(), gitlabProjectId, projectLinkRequest);
        return ApiSuccessResponse.success("projectId", projectId);
    }

    @GetMapping("/status")
    public ApiResponse<Map<String, Boolean>> getLinkedStatus(@AuthUser User user) {
        boolean hasLinkedProject = userProjectService.hasLinkedProject(user.getId());
        return ApiSuccessResponse.success("hasLinkedProject", hasLinkedProject);
    }

    @GetMapping
    public ApiResponse<List<UserProjectResponse>> getUserProjects(
            @AuthUser User user,
            @RequestParam(name = "currentProjectId", defaultValue = "-1") Long currentProjectId) {
        List<UserProjectResponse> userProjects =
                userProjectService.getUserProjects(user, currentProjectId);
        return ApiSuccessResponse.success(userProjects);
    }
}
