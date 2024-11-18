package com.reviewping.coflo.domain.project.repository;

import static com.reviewping.coflo.global.error.ErrorCode.PROJECT_NOT_EXIST;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByGitlabProjectId(Long gitlabProjectId);

    @Query(
            "SELECT COUNT(r) FROM Review r "
                    + "JOIN r.mrInfo m "
                    + "JOIN m.project p "
                    + "WHERE p.id = :projectId")
    Long findReviewCountByProjectId(@Param("projectId") Long projectId);

    default Project getById(Long projectId) {
        return findById(projectId).orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));
    }

    default Project getByGitlabProjectId(Long gitlabProjectId) {
        return findByGitlabProjectId(gitlabProjectId)
                .orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));
    }
}
