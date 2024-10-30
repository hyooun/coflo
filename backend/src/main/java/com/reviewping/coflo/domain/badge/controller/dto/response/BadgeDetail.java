package com.reviewping.coflo.domain.badge.controller.dto.response;

import com.reviewping.coflo.domain.badge.entity.Badge;

public record BadgeDetail(Long userBadgeId, String name, String description, String imageUrl) {
    public static BadgeDetail of(Long userBadgeId, Badge badge) {
        return new BadgeDetail(
                userBadgeId, badge.getName(), badge.getDescription(), badge.getImageUrl());
    }
}
