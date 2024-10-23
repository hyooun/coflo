package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {
    Boolean existsByUserId(Long userId);

    Optional<GitlabAccount> findFirstByUserIdOrderByIdAsc(Long userId);
}
