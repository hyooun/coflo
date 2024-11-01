package com.reviewping.coflo.domain.project.entity;

import java.util.Arrays;

public enum GraphType {
    TOTAL("total"),
    INDIVIDUAL("individual");

    private final String type;

    GraphType(String type) {
        this.type = type;
    }

    public static GraphType of(String typeStr) {
        return Arrays.stream(GraphType.values())
                .filter(gt -> gt.type.equals(typeStr))
                .findFirst()
                .orElse(TOTAL);
    }
}
