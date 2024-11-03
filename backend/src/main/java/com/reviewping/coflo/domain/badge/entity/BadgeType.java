package com.reviewping.coflo.domain.badge.entity;

import lombok.Getter;

@Getter
public enum BadgeType {
    FIRST_ADVENTURER(1L),
    PROJECT_MASTER(3L),
    REGULAR_CUSTOMER(7L);

    private final Long id;

    BadgeType(Long id) {
        this.id = id;
    }
}
