package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.domain.mapper.IndividualMapper;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.ArrayList;
import java.util.List;

public class IndividualScoreCalculator extends ScoreCalculator<UserProjectIndividualScoreResponse> {

    public IndividualScoreCalculator(CalculationType calculationType) {
        super(calculationType);
    }

    public UserProjectIndividualScoreResponse calculateScore(
            ProjectWeek projectWeek, List<UserProjectScore> userProjectScores) {
        List<CodeQualityScoreResponse> codeQualityScores =
                processMapper(userProjectScores, new IndividualMapper());
        List<CodeQualityScoreResponse> responseScores =
                calculateCumulativeCodeQualityScores(codeQualityScores);
        return new UserProjectIndividualScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), responseScores);
    }

    private List<CodeQualityScoreResponse> calculateCumulativeCodeQualityScores(
            List<CodeQualityScoreResponse> codeQualityScores) {
        if (calculationType == CalculationType.ACQUISITION) return codeQualityScores;

        List<CodeQualityScoreResponse> cumulativeCodeQualityScores = new ArrayList<>();
        for (CodeQualityScoreResponse codeQualityScore : codeQualityScores) {
            List<ScoreOfWeekResponse> cumulativeScores = new ArrayList<>();
            long cumulativeScore = 0;
            for (ScoreOfWeekResponse weeklyScore : codeQualityScore.scoreOfWeek()) {
                cumulativeScore += weeklyScore.score();
                cumulativeScores.add(new ScoreOfWeekResponse(weeklyScore.week(), cumulativeScore));
            }
            cumulativeCodeQualityScores.add(
                    new CodeQualityScoreResponse(
                            codeQualityScore.codeQualityName(), cumulativeScores));
        }
        return cumulativeCodeQualityScores;
    }
}
