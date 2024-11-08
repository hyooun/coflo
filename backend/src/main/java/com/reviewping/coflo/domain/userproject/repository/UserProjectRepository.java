package com.reviewping.coflo.domain.userproject.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_PROJECT_NOT_EXIST;

import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProjectRepository
        extends JpaRepository<UserProject, Long>, UserProjectRepositoryCustom {
    Boolean existsByGitlabAccountIdAndProjectId(Long gitlabAccountId, Long projectId);

    Boolean existsByGitlabAccountUserId(Long userId);

    Optional<UserProject> findByProjectAndGitlabAccount(
            Project project, GitlabAccount gitlabAccount);

    @Query("SELECT COUNT(up) FROM UserProject up WHERE up.gitlabAccount.id IN :gitlabAccountIds")
    Long countByGitlabAccountIds(@Param("gitlabAccountIds") List<Long> gitlabAccountIds);

    @EntityGraph(attributePaths = {"project", "userProjectScores", "gitlabAccount"})
    List<UserProject> findAll();

    @Query(
            "SELECT up FROM UserProject up JOIN up.gitlabAccount gl JOIN gl.user u "
                    + "WHERE u.id = :userId ORDER BY up.createdDate DESC LIMIT 1")
    UserProject findByUserId(@Param("userId") Long userId);

    default UserProject getByProjectAndGitlabAccount(Project project, GitlabAccount gitlabAccount) {
        return findByProjectAndGitlabAccount(project, gitlabAccount)
                .orElseThrow(() -> new BusinessException(USER_PROJECT_NOT_EXIST));
    }
}
