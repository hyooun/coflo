package com.reviewping.coflo.domain.customPrompt.controller.dto.response;

import lombok.Builder;

@Builder
public record CustomPromptResponse(Long customPromptId, String title, String content) {}
