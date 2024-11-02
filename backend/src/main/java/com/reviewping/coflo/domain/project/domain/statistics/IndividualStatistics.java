package com.reviewping.coflo.domain.project.domain.statistics;

import com.reviewping.coflo.domain.codequality.entity.CodeQualityCode;
import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class IndividualStatistics
        extends UserStatistics<CodeQualityCode, CodeQualityScoreResponse> {
    @Override
    public Collector<UserProjectScore, ?, Map<CodeQualityCode, List<UserProjectScore>>>
            getCollector() {
        return Collectors.groupingBy(UserProjectScore::getCodeQualityCode);
    }

    @Override
    public Function<Map.Entry<CodeQualityCode, List<UserProjectScore>>, CodeQualityScoreResponse>
            getMapper() {
        return entry -> {
            CodeQualityCode codeQuality = entry.getKey();
            List<UserProjectScore> scoresOfCodeQuality = entry.getValue();
            List<ScoreOfWeekResponse> scoreOfWeekResponses =
                    scoresOfCodeQuality.stream().map(ScoreOfWeekResponse::of).toList();
            return new CodeQualityScoreResponse(codeQuality.getName(), scoreOfWeekResponses);
        };
    }
}
