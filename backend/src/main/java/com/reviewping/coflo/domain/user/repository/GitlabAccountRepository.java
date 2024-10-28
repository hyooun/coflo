package com.reviewping.coflo.domain.user.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_GITLAB_ACCOUNT_NOT_EXIST;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {

    @Query("SELECT ga FROM GitlabAccount ga WHERE ga.user.id = :userId ORDER BY ga.id ASC")
    Optional<GitlabAccount> findFirstByUserIdOrderByIdAsc(@Param("userId") Long userId);

    @Query(
            "SELECT ga FROM GitlabAccount ga "
                    + "JOIN ga.userProjects up "
                    + "WHERE ga.user.id = :userId AND up.project.gitlabProjectId = :projectId")
    Optional<GitlabAccount> findGitlabAccountByUserIdAndProjectId(
            @Param("userId") Long userId, @Param("projectId") Long projectId);

    default GitlabAccount getFirstByUserId(Long userId) {
        return findFirstByUserIdOrderByIdAsc(userId)
                .orElseThrow(() -> new BusinessException(USER_GITLAB_ACCOUNT_NOT_EXIST));
    }
}
