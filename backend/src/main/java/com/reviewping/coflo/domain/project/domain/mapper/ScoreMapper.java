package com.reviewping.coflo.domain.project.domain.mapper;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;

public abstract class ScoreMapper<K, T> {

    public abstract Collector<UserProjectScore, ?, Map<K, List<UserProjectScore>>> getCollector();

    public abstract Function<Map.Entry<K, List<UserProjectScore>>, T> getMapper();
}
