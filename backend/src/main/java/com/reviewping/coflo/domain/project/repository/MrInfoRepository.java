package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.MrInfo;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MrInfoRepository extends JpaRepository<MrInfo, Long> {
    Optional<MrInfo> findByProjectIdAndGitlabMrIid(Long projectId, Long iid);

    Boolean existsByGitlabMrIid(Long gitlabMrIid);

    default MrInfo getByProjectIdAndGitlabMrIid(Long projectId, Long iid) {
        return findByProjectIdAndGitlabMrIid(projectId, iid)
                .orElseThrow(() -> new BusinessException(ErrorCode.MR_INFO_NOT_EXIST));
    }
}
