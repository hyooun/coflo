package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.domain.CalculationType;
import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

public abstract class ScoreCalculator<T, A, R> {

    protected final CalculationType calculationType;

    public ScoreCalculator(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public R process(ProjectWeek projectWeek, List<UserProjectScore> userProjectScores) {
        List<A> processedScores =
                userProjectScores.stream().collect(grouping()).entrySet().stream()
                        .map(mapper())
                        .toList();
        List<A> finalScores = calculate(processedScores);
        return response(projectWeek, finalScores);
    }

    protected abstract Collector<UserProjectScore, ?, Map<T, List<UserProjectScore>>> grouping();

    protected abstract Function<Map.Entry<T, List<UserProjectScore>>, A> mapper();

    protected abstract List<A> calculate(List<A> scores);

    protected abstract R response(ProjectWeek projectWeek, List<A> scores);
}
