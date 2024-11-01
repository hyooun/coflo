package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;

public interface UserProjectScoreRepositoryCustom {
    List<UserProjectScore> findUserProjectScores(Long userId, Long projectId, int week);

    List<UserProjectScore> findTopUserProjectScores(
            Long userId, Long projectId, int week, int limit);
}
