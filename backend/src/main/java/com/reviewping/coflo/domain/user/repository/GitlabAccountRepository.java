package com.reviewping.coflo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {
	Boolean existsByUserId(Long userId);
}
