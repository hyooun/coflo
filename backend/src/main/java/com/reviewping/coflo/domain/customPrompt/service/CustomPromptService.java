package com.reviewping.coflo.domain.customPrompt.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomPromptService {

    private final CustomPromptRepository customPromptRepository;

    public void saveCustomPrompt(Project project) {
        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
    }

    @Transactional
    public void updateCustomPrompt(String content, Long customPromptId) {
        CustomPrompt customPrompt = findById(customPromptId);
        customPrompt.setContent(content);
    }

    public void deleteCustomPrompt(Long customPromptId) {
        CustomPrompt customPrompt = findById(customPromptId);
        customPromptRepository.delete(customPrompt);
    }

    private CustomPrompt findById(Long customPromptId) {
        return customPromptRepository
                .findById(customPromptId)
                .orElseThrow(() -> new BusinessException(CUSTOM_PROMPT_NOT_EXIST));
    }
}
