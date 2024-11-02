package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectScoreRepository
        extends JpaRepository<UserProjectScore, Long>, UserProjectScoreRepositoryCustom {

    @Query(
            "SELECT SUM(ups.totalScore) "
                    + "FROM UserProjectScore ups "
                    + "WHERE ups.userProject.id = :userProjectId AND ups.week = :week "
                    + "GROUP BY ups.week, ups.userProject.id")
    Integer getTotalScoreSumByUserProjectIdAndWeek(
            @Param("userProjectId") Long userProjectId, @Param("week") int week);
}
