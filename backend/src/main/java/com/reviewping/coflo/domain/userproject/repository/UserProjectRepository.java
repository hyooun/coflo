package com.reviewping.coflo.domain.userproject.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_PROJECT_NOT_EXIST;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectRepository
        extends JpaRepository<UserProject, Long>, UserProjectRepositoryCustom {
    Boolean existsByGitlabAccountIdAndProjectId(Long gitlabAccountId, Long projectId);

    Boolean existsByGitlabAccountUserId(Long userId);

    Optional<UserProject> findByProjectAndGitlabAccount(
            Project project, GitlabAccount gitlabAccount);

    List<UserProject> findByProject(Project project);

    default UserProject getByProjectAndGitlabAccount(Project project, GitlabAccount gitlabAccount) {
        return findByProjectAndGitlabAccount(project, gitlabAccount)
                .orElseThrow(() -> new BusinessException(USER_PROJECT_NOT_EXIST));
    }

    @Query(
            value =
                    """
                            SELECT up.*
                            FROM user_project_score ups
                            JOIN user_project up ON ups.user_project_id = up.id
                            JOIN gitlab_account ga ON up.gitlab_account_id = ga.id
                            JOIN "user" u ON ga.user_id = u.id
                            WHERE up.project_id = :projectId
                            AND ups.week = :week
                            AND u.id != :userId
                            GROUP BY up.id
                            ORDER BY SUM(ups.total_score) DESC
                            LIMIT 5
                            """,
            nativeQuery = true)
    List<UserProject> findTopScoreUserProjectsOfWeek(
            @Param("projectId") Long projectId,
            @Param("week") int week,
            @Param("userId") Long userId);
}
