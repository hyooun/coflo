package com.reviewping.coflo.domain.badge.controller.dto.response;

import java.util.List;

public record BadgeResponse(Long mainBadgeCodeId, List<BadgeDetail> badgeDetails) {
    public static BadgeResponse of(Long mainBadgeCodeId, List<BadgeDetail> badgeDetails) {
        return new BadgeResponse(mainBadgeCodeId, badgeDetails);
    }
}
