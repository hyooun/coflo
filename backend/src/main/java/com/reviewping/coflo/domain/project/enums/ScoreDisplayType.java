package com.reviewping.coflo.domain.project.enums;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_SCORE_DISPLAY_TYPE_NOT_EXIST;

import com.reviewping.coflo.global.error.exception.BusinessException;
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
                .orElseThrow(() -> new BusinessException(PROJECT_SCORE_DISPLAY_TYPE_NOT_EXIST));
    }
}
