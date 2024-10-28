package com.reviewping.coflo.domain.customPrompt.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.customPrompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetCustomPromptService {

    private final CustomPromptRepository customPromptRepository;

    public CustomPromptResponse getCustomPrompt(Long projectId) {
        CustomPrompt customPrompt = findByProjectId(projectId);

        CustomPromptResponse customPromptResponse =
                CustomPromptResponse.builder()
                        .customPromptId(customPrompt.getId())
                        .content(customPrompt.getContent().replace("\n", "<br>"))
                        .build();
        return customPromptResponse;
    }

    private CustomPrompt findByProjectId(Long projectId) {
        return customPromptRepository
                .findByProjectId(projectId)
                .orElseThrow(() -> new BusinessException(CUSTOM_PROMPT_NOT_EXIST));
    }
}
