package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TotalScoreCalculator
        extends ScoreCalculator<Integer, ScoreOfWeekResponse, UserProjectTotalScoreResponse> {

    public TotalScoreCalculator(CalculationType calculationType) {
        super(calculationType);
    }

    @Override
    protected Collector<UserProjectScore, ?, Map<Integer, List<UserProjectScore>>> grouping() {
        return Collectors.groupingBy(UserProjectScore::getWeek);
    }

    @Override
    protected Function<Map.Entry<Integer, List<UserProjectScore>>, ScoreOfWeekResponse> mapper() {
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

    @Override
    protected List<ScoreOfWeekResponse> calculate(List<ScoreOfWeekResponse> scores) {
        if (calculationType == CalculationType.ACQUISITION) return scores;
        List<ScoreOfWeekResponse> cumulativeScores = new ArrayList<>();
        long cumulativeScore = 0;
        for (ScoreOfWeekResponse score : scores) {
            cumulativeScore += score.score();
            cumulativeScores.add(new ScoreOfWeekResponse(score.week(), cumulativeScore));
        }
        return cumulativeScores;
    }

    @Override
    protected UserProjectTotalScoreResponse response(
            ProjectWeek projectWeek, List<ScoreOfWeekResponse> scores) {
        return new UserProjectTotalScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), scores);
    }
}
