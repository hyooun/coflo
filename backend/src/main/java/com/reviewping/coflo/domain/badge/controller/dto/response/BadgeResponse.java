package com.reviewping.coflo.domain.badge.controller.dto.response;

import java.util.List;

public record BadgeResponse(Long mainUserBadgeId, List<BadgeDetail> badgeDetails) {
    public static BadgeResponse of(Long mainUserBadgeId, List<BadgeDetail> badgeDetails) {
        return new BadgeResponse(mainUserBadgeId, badgeDetails);
    }
}
