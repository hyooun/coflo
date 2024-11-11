package com.reviewping.coflo.domain.badge.controller;

import com.reviewping.coflo.domain.badge.controller.dto.request.MainBadgeRequest;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.service.BadgeService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.aop.LogExecution;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@LogExecution
@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    @Operation(summary = "로그인 된 사용자의 뱃지 정보 조회", description = "대표 뱃지 ID와 모든 뱃지 리스트, 사용자 획득 여부 조회")
    public ApiResponse<BadgeResponse> getBadgeInfo(@AuthUser User user) {
        return ApiSuccessResponse.success(badgeService.getBadgeInfo(user));
    }

    @PatchMapping
    @Operation(summary = "로그인 된 사용자의 대표 뱃지 변경")
    public ApiResponse<Void> updateMainBadge(
            @AuthUser User user, @RequestBody MainBadgeRequest mainBadgeRequest) {
        badgeService.updateMainBadge(user, mainBadgeRequest.badgeCodeId());
        return ApiSuccessResponse.success();
    }
}
