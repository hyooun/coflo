package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {

    Optional<GitlabAccount> findFirstByUserOrderByIdAsc(User user);

    @Query(
            "SELECT ga FROM GitlabAccount ga "
                    + "JOIN ga.userProjects up "
                    + "WHERE ga.user.id = :userId AND up.project.gitlabProjectId = :projectId")
    Optional<GitlabAccount> findGitlabAccountByUserIdAndProjectId(
            @Param("userId") Long userId, @Param("projectId") Long projectId);
}
