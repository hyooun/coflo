package com.reviewping.coflo.domain.userproject.controller;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.controller.dto.request.ProjectLinkRequest;
import com.reviewping.coflo.domain.userproject.controller.dto.response.UserProjectResponse;
import com.reviewping.coflo.domain.userproject.service.UserProjectService;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-project")
public class UserProjectController {

    private final UserProjectService userProjectService;

    @PostMapping("/{gitlabProjectId}")
    @Operation(summary = "프로젝트 연동 추가")
    public ApiResponse<Map<String, Long>> addUserProject(
            @AuthUser User user,
            @PathVariable("gitlabProjectId") Long gitlabProjectId,
            @RequestBody(required = false) ProjectLinkRequest projectLinkRequest) {
        Long projectId =
                userProjectService.linkGitlabProject(user, gitlabProjectId, projectLinkRequest);
        return ApiSuccessResponse.success("projectId", projectId);
    }

    @DeleteMapping("/{gitlabProjectId}")
    @Operation(summary = "연동된 프로젝트 삭제")
    public ApiResponse<Map<String, Long>> deleteUserProject(
            @AuthUser User user, @PathVariable("gitlabProjectId") Long gitlabProjectId) {
        Long projectId = userProjectService.unlinkGitlabProject(user, gitlabProjectId);
        return ApiSuccessResponse.success("projectId", projectId);
    }

    @GetMapping("/status")
    @Operation(summary = "프로젝트 연동 여부 조회")
    public ApiResponse<Map<String, Boolean>> getLinkedStatus(@AuthUser User user) {
        boolean hasLinkedProject = userProjectService.hasLinkedProject(user.getId());
        return ApiSuccessResponse.success("hasLinkedProject", hasLinkedProject);
    }

    @GetMapping
    @Operation(summary = "사용자의 연동된 프로젝트 리스트 조회")
    public ApiResponse<List<UserProjectResponse>> getUserProjects(
            @AuthUser User user,
            @RequestParam(name = "currentProjectId", defaultValue = "-1") Long currentProjectId) {
        List<UserProjectResponse> userProjects =
                userProjectService.getUserProjects(user, currentProjectId);
        return ApiSuccessResponse.success(userProjects);
    }
}
