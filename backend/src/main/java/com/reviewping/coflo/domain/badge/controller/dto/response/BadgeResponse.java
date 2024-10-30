package com.reviewping.coflo.domain.badge.controller.dto.response;

import java.util.List;

public record BadgeResponse(Long mainBadgeId, List<BadgeDetail> badgeDetails) {
    public static BadgeResponse of(Long mainBadgeId, List<BadgeDetail> badgeDetail) {
        return new BadgeResponse(mainBadgeId, badgeDetail);
    }
}
