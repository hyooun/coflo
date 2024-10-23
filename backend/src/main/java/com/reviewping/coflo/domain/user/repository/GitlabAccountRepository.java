package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {
    Boolean existsByUserId(Long userId);

    Optional<GitlabAccount> findFirstByUserIdOrderByIdAsc(Long userId);
}
