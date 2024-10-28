package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    Boolean existsByGitlabAccountIdAndProjectId(Long gitlabAccountId, Long projectId);

    Boolean existsByGitlabAccountUserId(Long userId);
}
