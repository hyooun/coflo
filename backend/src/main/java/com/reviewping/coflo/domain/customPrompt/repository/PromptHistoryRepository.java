package com.reviewping.coflo.domain.customPrompt.repository;

import com.reviewping.coflo.domain.customPrompt.entity.PromptHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptHistoryRepository extends JpaRepository<PromptHistory, Long> {
    long countByUserId(Long userId);
}
