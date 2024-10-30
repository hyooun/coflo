package com.reviewping.coflo.domain.customPrompt.repository;

import static com.reviewping.coflo.global.error.ErrorCode.CUSTOM_PROMPT_NOT_EXIST;

import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomPromptRepository extends JpaRepository<CustomPrompt, Long> {
    Optional<CustomPrompt> findByProjectId(Long projectId);

    default CustomPrompt getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(CUSTOM_PROMPT_NOT_EXIST));
    }
}
