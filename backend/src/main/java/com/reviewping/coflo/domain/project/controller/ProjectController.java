package com.reviewping.coflo.domain.project.controller;

import com.reviewping.coflo.domain.project.controller.response.ProjectTeamDetailResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamRewardResponse;
import com.reviewping.coflo.domain.project.service.ProjectStatisticsService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectStatisticsService projectStatisticsService;

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectTeamDetailResponse> getProjectInfoDetail(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(projectStatisticsService.getTeamDetail(user, projectId));
    }

    @GetMapping("/{projectId}/scores")
    public ApiResponse<ProjectTeamRewardResponse> getProjectTeamScore(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(
                projectStatisticsService.getTeamScore(user.getId(), projectId));
    }
}
