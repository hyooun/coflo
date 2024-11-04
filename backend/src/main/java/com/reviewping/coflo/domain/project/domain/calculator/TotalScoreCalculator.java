package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectTotalScoreResponse;
import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.domain.mapper.TotalMapper;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.ArrayList;
import java.util.List;

public class TotalScoreCalculator extends ScoreCalculator<UserProjectTotalScoreResponse> {

    public TotalScoreCalculator(CalculationType calculationType) {
        super(calculationType);
    }

    public UserProjectTotalScoreResponse calculateScore(
            ProjectWeek projectWeek, List<UserProjectScore> userProjectScores) {
        List<ScoreOfWeekResponse> weeklyScores =
                processMapper(userProjectScores, new TotalMapper());
        List<ScoreOfWeekResponse> responseScores = calculateCumulativeScores(weeklyScores);
        return new UserProjectTotalScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), responseScores);
    }

    private List<ScoreOfWeekResponse> calculateCumulativeScores(
            List<ScoreOfWeekResponse> weeklyScores) {
        if (calculationType == CalculationType.ACQUISITION) return weeklyScores;

        List<ScoreOfWeekResponse> cumulativeScores = new ArrayList<>();
        long cumulativeScore = 0;
        for (ScoreOfWeekResponse weeklyScore : weeklyScores) {
            cumulativeScore += weeklyScore.score();
            cumulativeScores.add(new ScoreOfWeekResponse(weeklyScore.week(), cumulativeScore));
        }
        return cumulativeScores;
    }
}
