package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;

public interface UserProjectScoreRepositoryCustom {
    List<UserProjectScore> findTopUserProjectScores(Long projectId, int week, Long userId);
}
