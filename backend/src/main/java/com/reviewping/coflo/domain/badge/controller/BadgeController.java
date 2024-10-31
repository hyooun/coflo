package com.reviewping.coflo.domain.badge.controller;

import com.reviewping.coflo.domain.badge.controller.dto.request.MainBadgeRequest;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.service.BadgeService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping
    public ApiResponse<BadgeResponse> getBadgeInfo(@AuthUser User user) {
        return ApiSuccessResponse.success(badgeService.getBadgeInfo(user));
    }

    @PatchMapping
    public ApiResponse<Void> updateMainBadge(
            @AuthUser User user, @RequestBody MainBadgeRequest mainBadgeRequest) {
        badgeService.updateMainBadge(user, mainBadgeRequest.badgeCodeId());
        return ApiSuccessResponse.success();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteMainBadge(@AuthUser User user) {
        badgeService.deleteMainBadge(user);
        return ApiSuccessResponse.success();
    }
}
