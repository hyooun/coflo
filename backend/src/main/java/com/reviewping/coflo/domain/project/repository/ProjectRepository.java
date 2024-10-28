package com.reviewping.coflo.domain.project.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByGitlabProjectId(Long gitlabProjectId);

    default Project findProjectById(Long projectId) {
        return findById(projectId).orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));
    }
}
