package com.reviewping.coflo.domain.customPrompt.service;

import com.reviewping.coflo.domain.customPrompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.global.error.ErrorCode;
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
        CustomPrompt customPrompt = customPromptRepository.findByProjectId(projectId);
        if (customPrompt == null) {
            throw new BusinessException(ErrorCode.CUSTOM_PROMPT_NOT_EXIST);
        }

        CustomPromptResponse customPromptResponse =
                CustomPromptResponse.builder()
                        .customPromptId(customPrompt.getId())
                        .title(customPrompt.getTitle())
                        .content(customPrompt.getContent().replace("\n", "<br>"))
                        .build();
        return customPromptResponse;
    }
}
