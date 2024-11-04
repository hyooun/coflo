package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ScoreDisplayType;

public class ScoreCalculatorFactory {
    public static ScoreCalculator<?> createCalculator(
            ScoreDisplayType displayType, CalculationType calculationType) {
        if (displayType == ScoreDisplayType.INDIVIDUAL) {
            return new IndividualScoreCalculator(calculationType);
        } else {
            return new TotalScoreCalculator(calculationType);
        }
    }
}
