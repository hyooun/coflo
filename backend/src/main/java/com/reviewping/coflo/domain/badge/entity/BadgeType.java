package com.reviewping.coflo.domain.badge.entity;

import lombok.Getter;

@Getter
public enum BadgeType {
    FIRST_ADVENTURER(1L),
    REVIEW_FINDER(2L),
    PROJECT_MASTER(3L),
    CODE_MASTER(5L),
    LUCKY_FIND(6L),
    REGULAR_CUSTOMER(7L),
    CONQUEROR(11L),
    PROMPT_CREATER(12L);

    private final Long id;

    BadgeType(Long id) {
        this.id = id;
    }
}
