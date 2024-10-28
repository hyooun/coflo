package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.MrInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MrInfoRepository extends JpaRepository<MrInfo, Long> {
    Optional<MrInfo> findByProjectIdAndGitlabMrIid(Long projectId, Long iid);

    Boolean existsByGitlabMrIid(Long gitlabMrIid);
}
