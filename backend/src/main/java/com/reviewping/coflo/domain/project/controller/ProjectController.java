package com.reviewping.coflo.domain.project.controller;

import com.reviewping.coflo.domain.project.controller.response.ProjectTeamDetailResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamRewardResponse;
import com.reviewping.coflo.domain.project.entity.GraphType;
import com.reviewping.coflo.domain.project.service.ProjectTeamStatisticsService;
import com.reviewping.coflo.domain.project.service.ProjectUserStatisticsService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectTeamStatisticsService projectTeamStatisticsService;
    private final ProjectUserStatisticsService projectUserStatisticsService;

    @GetMapping("/{projectId}")
    public ApiResponse<ProjectTeamDetailResponse> getProjectInfoDetail(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(
                projectTeamStatisticsService.getTeamDetail(user, projectId));
    }

    @GetMapping("/{projectId}/scores")
    public ApiResponse<ProjectTeamRewardResponse> getProjectTeamScore(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(
                projectTeamStatisticsService.getTeamScore(user, projectId));
    }

    @GetMapping("/{projectId}/cumulative")
    ApiResponse<?> getProjectUserCumulativeScore(
            @AuthUser User user,
            @PathVariable("projectId") Long projectId,
            @RequestParam(name = "type", required = false) GraphType type,
            @RequestParam(name = "period", required = false, defaultValue = "7") Integer period) {
        if (type == GraphType.INDIVIDUAL) {
            return ApiSuccessResponse.success(
                    projectUserStatisticsService.getIndividualCumulativeScore(
                            user, projectId, period));
        }
        return ApiSuccessResponse.success(
                projectUserStatisticsService.getTotalCumulativeScore(user, projectId, period));
    }

    @GetMapping("/{projectId}/acquisition")
    ApiResponse<?> getProjectUserAcquisitionScore(
            @AuthUser User user,
            @PathVariable("projectId") Long projectId,
            @RequestParam(name = "type", required = false) GraphType type,
            @RequestParam(name = "period", required = false, defaultValue = "7") Integer period) {
        if (type == GraphType.INDIVIDUAL) {
            return ApiSuccessResponse.success(
                    projectUserStatisticsService.getIndividualAcquisitionScore(
                            user, projectId, period));
        }
        return ApiSuccessResponse.success(
                projectUserStatisticsService.getTotalAcquisitionScore(user, projectId, period));
    }
}
