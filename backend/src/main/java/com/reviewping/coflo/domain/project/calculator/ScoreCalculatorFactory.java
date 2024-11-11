package com.reviewping.coflo.domain.project.calculator;

import static com.reviewping.coflo.domain.project.enums.CalculationType.ACQUISITION;
import static com.reviewping.coflo.domain.project.enums.CalculationType.CUMULATIVE;
import static com.reviewping.coflo.domain.project.enums.ScoreDisplayType.INDIVIDUAL;
import static com.reviewping.coflo.domain.project.enums.ScoreDisplayType.TOTAL;

import com.reviewping.coflo.domain.project.enums.CalculationType;
import com.reviewping.coflo.domain.project.enums.ScoreDisplayType;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ScoreCalculatorFactory {

    private Map<String, ScoreCalculator> map = new HashMap<>();

    public ScoreCalculatorFactory() {
        map.put(buildKey(INDIVIDUAL, ACQUISITION), new IndividualScoreCalculator(ACQUISITION));
        map.put(buildKey(INDIVIDUAL, CUMULATIVE), new IndividualScoreCalculator(CUMULATIVE));
        map.put(buildKey(TOTAL, ACQUISITION), new TotalScoreCalculator(ACQUISITION));
        map.put(buildKey(TOTAL, CUMULATIVE), new TotalScoreCalculator(CUMULATIVE));
    }

    public ScoreCalculator get(CalculationType calculationType, ScoreDisplayType scoreDisplayType) {
        String key = buildKey(scoreDisplayType, calculationType);
        if (!map.containsKey(key)) {
            throw new BusinessException(ErrorCode.PROJECT_STATISTICS_TYPE_NOT_EXIST);
        }
        return map.get(key);
    }

    private String buildKey(ScoreDisplayType scoreDisplayType, CalculationType calculationType) {
        return scoreDisplayType.name() + "_" + calculationType.name();
    }
}
