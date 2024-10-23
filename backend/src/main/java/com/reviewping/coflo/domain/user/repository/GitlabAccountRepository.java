package com.reviewping.coflo.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {
	Boolean existsByUserId(Long userId);

	Optional<GitlabAccount> findFirstByUserIdOrderByIdAsc(Long userId);
}
