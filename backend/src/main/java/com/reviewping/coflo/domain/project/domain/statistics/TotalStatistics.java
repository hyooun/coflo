package com.reviewping.coflo.domain.project.domain.statistics;

import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TotalStatistics extends UserStatistics<Integer, ScoreOfWeekResponse> {
    @Override
    public Collector<UserProjectScore, ?, Map<Integer, List<UserProjectScore>>> getCollector() {
        return Collectors.groupingBy(UserProjectScore::getWeek);
    }

    @Override
    public Function<Map.Entry<Integer, List<UserProjectScore>>, ScoreOfWeekResponse> getMapper() {
        return entry -> {
            Integer week = entry.getKey();
            List<UserProjectScore> userProjectScoresOfWeek = entry.getValue();
            long totalScoreOfWeekSum =
                    userProjectScoresOfWeek.stream()
                            .mapToLong(UserProjectScore::getTotalScore)
                            .sum();
            return new ScoreOfWeekResponse(week, totalScoreOfWeekSum);
        };
    }
}
