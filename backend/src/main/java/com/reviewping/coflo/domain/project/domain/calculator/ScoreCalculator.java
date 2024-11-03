package com.reviewping.coflo.domain.project.domain.calculator;

import com.reviewping.coflo.domain.project.domain.ProjectWeek;
import com.reviewping.coflo.domain.project.domain.ScoreType;
import com.reviewping.coflo.domain.project.domain.mapper.ScoreMapper;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;

public abstract class ScoreCalculator<R> {

    protected final ScoreType scoreType;

    public ScoreCalculator(ScoreType scoreType) {
        this.scoreType = scoreType;
    }

    public <K, T> List<T> processMapper(
            List<UserProjectScore> userProjectScores, ScoreMapper<K, T> scoreMapper) {
        return userProjectScores.stream().collect(scoreMapper.getCollector()).entrySet().stream()
                .map(scoreMapper.getMapper())
                .toList();
    }

    public abstract R calculateScore(
            ProjectWeek projectWeek, List<UserProjectScore> userProjectScores);
}
