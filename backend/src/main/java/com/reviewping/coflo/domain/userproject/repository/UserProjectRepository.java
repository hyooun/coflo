package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository
        extends JpaRepository<UserProject, Long>, UserProjectRepositoryCustom {
    Boolean existsByGitlabAccountIdAndProjectId(Long gitlabAccountId, Long projectId);

    Boolean existsByUser(User user);
}
