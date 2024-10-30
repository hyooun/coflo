package com.reviewping.coflo.domain.badge.controller.dto.response;

import com.reviewping.coflo.domain.badge.entity.Badge;

public record BadgeDetail(Long badgeId, String name, String description, String imageUrl) {
    public static BadgeDetail of(Badge badge) {
        return new BadgeDetail(
                badge.getId(), badge.getName(), badge.getDescription(), badge.getImageUrl());
    }
}
