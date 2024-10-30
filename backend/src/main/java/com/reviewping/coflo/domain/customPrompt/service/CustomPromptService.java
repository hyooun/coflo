package com.reviewping.coflo.domain.customPrompt.service;

import com.reviewping.coflo.domain.customPrompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomPromptService {

    private final CustomPromptRepository customPromptRepository;

    // TODO: 프로젝트 처음 연동 시, 호출 필요
    public void saveCustomPrompt(Project project) {
        CustomPrompt customPrompt = CustomPrompt.builder().project(project).build();
        customPromptRepository.save(customPrompt);
    }

    @Transactional
    public void updateCustomPrompt(String content, Long customPromptId) {
        CustomPrompt customPrompt = customPromptRepository.getById(customPromptId);
        customPrompt.updateContent(content);
    }

    public CustomPromptResponse getCustomPrompt(Long projectId) {
        CustomPrompt customPrompt = customPromptRepository.getByProjectId(projectId);

        CustomPromptResponse customPromptResponse =
                CustomPromptResponse.builder()
                        .customPromptId(customPrompt.getId())
                        .content(customPrompt.getContent().replace("\n", "<br>"))
                        .build();
        return customPromptResponse;
    }
}
