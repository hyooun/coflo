package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectScoreRepository extends JpaRepository<UserProjectScore, Long> {
    List<UserProjectScore> findByUserProjectAndWeek(UserProject userProject, int week);
}
