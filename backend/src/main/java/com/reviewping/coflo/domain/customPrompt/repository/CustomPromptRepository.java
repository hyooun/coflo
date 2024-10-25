package com.reviewping.coflo.domain.customPrompt.repository;

import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomPromptRepository extends JpaRepository<CustomPrompt, Long> {
    Optional<CustomPrompt> findByProjectId(Long projectId);
}
