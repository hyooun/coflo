package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitlabAccountRepository extends JpaRepository<GitlabAccount, Long> {
    Boolean existsByUserId(Long userId);

    Optional<GitlabAccount> findFirstByUserOrderByIdAsc(User user);
}
