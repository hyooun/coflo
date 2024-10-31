package com.reviewping.coflo.domain.badge.controller.dto.response;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;

public record BadgeDetail(Long badgeCodeId, String name, String description, String imageUrl) {
    public static BadgeDetail of(BadgeCode badgeCode) {
        return new BadgeDetail(
                badgeCode.getId(),
                badgeCode.getName(),
                badgeCode.getDescription(),
                badgeCode.getImageUrl());
    }
}
