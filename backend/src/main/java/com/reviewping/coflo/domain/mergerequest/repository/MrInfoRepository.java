package com.reviewping.coflo.domain.mergerequest.repository;

import com.reviewping.coflo.domain.mergerequest.entity.MrInfo;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MrInfoRepository extends JpaRepository<MrInfo, Long> {
    Optional<MrInfo> findByProjectIdAndGitlabMrIid(Long projectId, Long iid);

    Boolean existsByGitlabMrIid(Long gitlabMrIid);

    default MrInfo getByProjectIdAndGitlabMrIid(Long projectId, Long iid) {
        return findByProjectIdAndGitlabMrIid(projectId, iid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MR_INFO_NOT_EXIST));
    }

    default MrInfo getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.MR_INFO_NOT_EXIST));
    }

    @Query(
            value =
                    "SELECT * FROM mr_info m WHERE m.project_id = :projectId AND"
                        + " m.gitlab_created_date BETWEEN :startDate AND :endDate ORDER BY"
                        + " (COALESCE(m.readability_score, 0) + COALESCE(m.consistency_score, 0) +"
                        + " COALESCE(m.reusability_score, 0) + COALESCE(m.reliability_score, 0) +"
                        + " COALESCE(m.security_score, 0) + COALESCE(m.maintainability_score, 0))"
                        + " DESC LIMIT 3",
            nativeQuery = true)
    List<MrInfo> findTop3MrInfoList(
            @Param("projectId") Long projectId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
