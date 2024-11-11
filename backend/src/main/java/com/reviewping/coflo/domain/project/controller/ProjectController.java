package com.reviewping.coflo.domain.project.controller;

import com.reviewping.coflo.domain.project.controller.response.ProjectTeamDetailResponse;
import com.reviewping.coflo.domain.project.controller.response.ProjectTeamRewardResponse;
import com.reviewping.coflo.domain.project.enums.CalculationType;
import com.reviewping.coflo.domain.project.enums.ScoreDisplayType;
import com.reviewping.coflo.domain.project.service.ProjectTeamStatisticsService;
import com.reviewping.coflo.domain.project.service.ProjectUserStatisticsService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@LogExecution
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectTeamStatisticsService projectTeamStatisticsService;
    private final ProjectUserStatisticsService projectUserStatisticsService;

    @GetMapping("/{projectId}")
    @Operation(summary = "프로젝트 상세 정보 조회")
    public ApiResponse<ProjectTeamDetailResponse> getProjectInfoDetail(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(
                projectTeamStatisticsService.getTeamDetail(user, projectId));
    }

    @GetMapping("/{projectId}/scores")
    @Operation(summary = "주간 6항목 점수 데이터 조회", description = "점수 총합 상위 5명에 대한 6항목 점수 데이터")
    public ApiResponse<ProjectTeamRewardResponse> getProjectTeamScore(
            @AuthUser User user, @PathVariable("projectId") Long projectId) {
        return ApiSuccessResponse.success(
                projectTeamStatisticsService.getTeamScore(user.getId(), projectId));
    }

    @GetMapping("/{projectId}/statistics")
    @Operation(summary = "개인별 성장 그래프 데이터 조회", description = "옵션에 따라 개인별, 팀별, 기간별 조회")
    ApiResponse<?> getProjectUserStatisticsScore(
            @AuthUser User user,
            @PathVariable("projectId") Long projectId,
            @RequestParam(name = "calculationType") CalculationType calculationType,
            @RequestParam(name = "scoreDisplayType") ScoreDisplayType scoreDisplayType,
            @RequestParam(name = "period", required = false, defaultValue = "7") Integer period) {
        return ApiSuccessResponse.success(
                projectUserStatisticsService.calculateScore(
                        user, projectId, period, calculationType, scoreDisplayType));
    }
}
