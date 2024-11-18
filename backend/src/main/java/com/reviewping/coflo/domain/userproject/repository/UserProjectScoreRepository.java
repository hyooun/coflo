package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectScoreRepository
        extends JpaRepository<UserProjectScore, Long>, UserProjectScoreRepositoryCustom {

    List<UserProjectScore> findAllByUserProjectAndWeek(UserProject userProject, int week);

    @Query(
            "SELECT ups FROM UserProjectScore ups "
                    + "WHERE ups.userProject.id = :userProjectId "
                    + "AND ups.week BETWEEN :startWeek AND :endWeek "
                    + "ORDER BY ups.week ASC")
    List<UserProjectScore> findByUserProjectIdAndWeekRange(
            @Param("userProjectId") Long userProjectId,
            @Param("startWeek") int startWeek,
            @Param("endWeek") int endWeek);

    @Query(
            "SELECT ups FROM UserProjectScore ups "
                    + "WHERE ups.userProject = :userProject "
                    + "AND ups.week = :week "
                    + "AND ups.codeQualityCode.id = :codeQualityCodeId")
    Optional<UserProjectScore> findScoreByProjectWeekAndCode(
            @Param("userProject") UserProject userProject,
            @Param("week") int week,
            @Param("codeQualityCodeId") Long codeQualityCodeId);
}
