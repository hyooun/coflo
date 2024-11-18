package com.reviewping.coflo.domain.badge.entity;

import lombok.Getter;

@Getter
public enum BadgeType {
    FIRST_ADVENTURER(1L),
    REVIEW_FINDER(2L),
    PROJECT_MASTER(3L),
    CODE_MASTER(4L),
    LUCKY_FIND(5L),
    REGULAR_CUSTOMER(6L),
    CONQUEROR(7L),
    PROMPT_CREATER(8L),
    LEGEND_ADVENTURER(9L);

    private final Long id;

    BadgeType(Long id) {
        this.id = id;
    }
}
