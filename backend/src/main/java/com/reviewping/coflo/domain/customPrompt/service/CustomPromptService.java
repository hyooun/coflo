package com.reviewping.coflo.domain.customPrompt.service;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.customPrompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customPrompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomPromptService {

    private final CustomPromptRepository customPromptRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void saveCustomPrompt(String title, String content, Long projectId) {
        Project project =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new BusinessException(PROJECT_NOT_EXIST));

        CustomPrompt customPrompt =
                CustomPrompt.builder().title(title).content(content).project(project).build();

        customPromptRepository.save(customPrompt);
    }
}
