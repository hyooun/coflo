package com.reviewping.coflo.domain.project.domain;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_CALCULATION_TYPE_NOT_EXIST;

import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Arrays;

public enum CalculationType {
    ACQUISITION("acquisition"),
    CUMULATIVE("cumulative");

    private final String type;

    CalculationType(String type) {
        this.type = type;
    }

    public static CalculationType of(String typeStr) {
        return Arrays.stream(CalculationType.values())
                .filter(st -> st.type.equals(typeStr))
                .findFirst()
                .orElseThrow(() -> new BusinessException(PROJECT_CALCULATION_TYPE_NOT_EXIST));
    }
}
