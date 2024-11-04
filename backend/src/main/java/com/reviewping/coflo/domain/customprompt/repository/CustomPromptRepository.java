package com.reviewping.coflo.domain.customprompt.repository;

import static com.reviewping.coflo.global.error.ErrorCode.CUSTOM_PROMPT_NOT_EXIST;

import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomPromptRepository extends JpaRepository<CustomPrompt, Long> {
    Optional<CustomPrompt> findByProjectId(Long projectId);

    default CustomPrompt getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(CUSTOM_PROMPT_NOT_EXIST));
    }

    default CustomPrompt getByProjectId(Long projectId) {
        return findByProjectId(projectId)
                .orElseThrow(() -> new BusinessException(CUSTOM_PROMPT_NOT_EXIST));
    }
}
