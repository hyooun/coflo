package com.reviewping.coflo.domain.project.domain;

import java.util.Arrays;

public enum CalculationType {
    CUMULATIVE("cumulative"),
    ACQUISITION("acquisition");

    private final String type;

    CalculationType(String type) {
        this.type = type;
    }

    public static CalculationType of(String typeStr) {
        return Arrays.stream(CalculationType.values())
                .filter(st -> st.type.equals(typeStr))
                .findFirst()
                .orElse(CUMULATIVE);
    }
}
