package com.reviewping.coflo.domain.project.domain.caculator;

import com.reviewping.coflo.domain.project.controller.response.CodeQualityScoreResponse;
import com.reviewping.coflo.domain.project.controller.response.ScoreOfWeekResponse;
import com.reviewping.coflo.domain.project.controller.response.UserProjectIndividualScoreResponse;
import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.entity.CodeQualityCode;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class IndividualScoreCalculator
        extends ScoreCalculator<
                CodeQualityCode, CodeQualityScoreResponse, UserProjectIndividualScoreResponse> {

    public IndividualScoreCalculator(CalculationType calculationType) {
        super(calculationType);
    }

    @Override
    protected Collector<UserProjectScore, ?, Map<CodeQualityCode, List<UserProjectScore>>>
            grouping() {
        return Collectors.groupingBy(UserProjectScore::getCodeQualityCode);
    }

    @Override
    protected Function<Map.Entry<CodeQualityCode, List<UserProjectScore>>, CodeQualityScoreResponse>
            mapper() {
        return entry -> {
            CodeQualityCode codeQuality = entry.getKey();
            List<UserProjectScore> scoresOfCodeQuality = entry.getValue();
            List<ScoreOfWeekResponse> scoreOfWeekResponses =
                    scoresOfCodeQuality.stream().map(ScoreOfWeekResponse::of).toList();
            return new CodeQualityScoreResponse(codeQuality.getName(), scoreOfWeekResponses);
        };
    }

    @Override
    protected List<CodeQualityScoreResponse> calculate(List<CodeQualityScoreResponse> scores) {
        if (calculationType == CalculationType.ACQUISITION) return scores;
        List<CodeQualityScoreResponse> cumulativeCodeQualityScores = new ArrayList<>();
        for (CodeQualityScoreResponse codeQualityScore : scores) {
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

    @Override
    protected UserProjectIndividualScoreResponse response(
            ProjectWeek projectWeek, List<CodeQualityScoreResponse> scores) {
        return new UserProjectIndividualScoreResponse(
                projectWeek.startDate(), projectWeek.endDate(), scores);
    }
}
