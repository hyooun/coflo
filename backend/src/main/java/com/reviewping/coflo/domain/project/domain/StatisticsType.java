package com.reviewping.coflo.domain.project.domain;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_STATISTICS_TYPE_NOT_EXIST;

import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Arrays;

public enum StatisticsType {
    ACQUISITION_INDIVIDUAL("acquisition_individual"),
    ACQUISITION_INTEGRATION("cumulative_integration"),
    CUMULATIVE_INDIVIDUAL("cumulative_individual"),
    CUMULATIVE_INTEGRATION("cumulative_integration");

    private final String type;

    StatisticsType(String type) {
        this.type = type;
    }

    public static StatisticsType of(
            CalculationType calculationType, ScoreDisplayType scoreDisplayType) {
        String typeStr = calculationType.name() + "_" + scoreDisplayType.name();
        return Arrays.stream(StatisticsType.values())
                .filter(st -> st.type.equals(typeStr))
                .findFirst()
                .orElseThrow(() -> new BusinessException(PROJECT_STATISTICS_TYPE_NOT_EXIST));
    }
}
