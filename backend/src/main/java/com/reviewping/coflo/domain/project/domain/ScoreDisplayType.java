package com.reviewping.coflo.domain.project.domain;

import java.util.Arrays;

public enum ScoreDisplayType {
    TOTAL("total"),
    INDIVIDUAL("individual");

    private final String type;

    ScoreDisplayType(String type) {
        this.type = type;
    }

    public static ScoreDisplayType of(String typeStr) {
        return Arrays.stream(ScoreDisplayType.values())
                .filter(gt -> gt.type.equals(typeStr))
                .findFirst()
                .orElse(TOTAL);
    }
}
