package com.reviewping.coflo.domain.project.domain.caculator;

import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ScoreDisplayType;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculatorFactory {

    public ScoreCalculator get(CalculationType calculationType, ScoreDisplayType scoreDisplayType) {
        switch (scoreDisplayType) {
            case INDIVIDUAL -> new IndividualScoreCalculator(calculationType);
            case TOTAL -> new TotalScoreCalculator(calculationType);
        }
        return null;
    }
}
