package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
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
    Long getTotalScoreSumByUserProjectIdAndWeek(
            @Param("userProjectId") Long userProjectId, @Param("week") int week);

    @Query(
            "SELECT ups FROM UserProjectScore ups "
                    + "WHERE ups.userProject.id = :userProjectId "
                    + "AND ups.codeQualityCode.id = :codeQualityId "
                    + "AND ups.week BETWEEN :startWeek AND :endWeek "
                    + "ORDER BY ups.week ASC")
    List<UserProjectScore> findByUserProjectIdAndCodeQualityIdAndWeekRange(
            @Param("userProjectId") Long userProjectId,
            @Param("codeQualityId") Long codeQualityId,
            @Param("startWeek") int startWeek,
            @Param("endWeek") int endWeek);
}
