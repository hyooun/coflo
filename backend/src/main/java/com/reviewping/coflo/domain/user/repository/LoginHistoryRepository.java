package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.LoginHistory;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
    boolean existsByUserIdAndLoginDate(Long userId, LocalDate loginDate);

    long countByUserId(Long userId);
}
