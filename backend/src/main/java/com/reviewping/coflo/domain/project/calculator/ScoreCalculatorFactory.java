package com.reviewping.coflo.domain.project.calculator;

import com.reviewping.coflo.domain.project.enums.CalculationType;
import com.reviewping.coflo.domain.project.enums.ScoreDisplayType;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculatorFactory {

    public ScoreCalculator get(CalculationType calculationType, ScoreDisplayType scoreDisplayType) {
        switch (scoreDisplayType) {
            case INDIVIDUAL -> {
                return new IndividualScoreCalculator(calculationType);
            }
            case TOTAL -> {
                return new TotalScoreCalculator(calculationType);
            }
            default -> throw new BusinessException(ErrorCode.PROJECT_STATISTICS_TYPE_NOT_EXIST);
        }
    }
}
