package com.reviewping.coflo.domain.customprompt.service;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.customprompt.controller.dto.response.CustomPromptResponse;
import com.reviewping.coflo.domain.customprompt.entity.CustomPrompt;
import com.reviewping.coflo.domain.customprompt.entity.PromptHistory;
import com.reviewping.coflo.domain.customprompt.repository.CustomPromptRepository;
import com.reviewping.coflo.domain.customprompt.repository.PromptHistoryRepository;
import com.reviewping.coflo.domain.user.entity.User;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomPromptService {

    private final CustomPromptRepository customPromptRepository;
    private final PromptHistoryRepository promptHistoryRepository;
    private final BadgeEventService badgeEventService;

    @Transactional
    public void updateCustomPrompt(User user, String content, Long customPromptId) {
        CustomPrompt customPrompt = customPromptRepository.getById(customPromptId);
        customPrompt.updateContent(content);
        promptHistoryRepository.save(new PromptHistory(user.getId(), LocalDateTime.now()));
        badgeEventService.eventUpdateCustomPrompt(user);
    }

    public CustomPromptResponse getCustomPrompt(Long projectId) {
        CustomPrompt customPrompt = customPromptRepository.getByProjectId(projectId);

        return CustomPromptResponse.builder()
                .customPromptId(customPrompt.getId())
                .content(customPrompt.getContent().replace("\n", "<br>"))
                .build();
    }
}
