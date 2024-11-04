package com.reviewping.coflo.domain.customprompt.repository;

import com.reviewping.coflo.domain.customprompt.entity.PromptHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptHistoryRepository extends JpaRepository<PromptHistory, Long> {
    long countByUserId(Long userId);
}
