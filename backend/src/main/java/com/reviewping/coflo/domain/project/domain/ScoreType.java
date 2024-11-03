package com.reviewping.coflo.domain.project.domain;

import java.util.Arrays;

public enum ScoreType {
    CUMULATIVE("cumulative"),
    ACQUISITION("acquisition");

    private final String type;

    ScoreType(String type) {
        this.type = type;
    }

    public static ScoreType of(String typeStr) {
        return Arrays.stream(ScoreType.values())
                .filter(st -> st.type.equals(typeStr))
                .findFirst()
                .orElse(CUMULATIVE);
    }
}
