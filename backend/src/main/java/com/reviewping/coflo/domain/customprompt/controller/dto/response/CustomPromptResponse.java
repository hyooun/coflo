package com.reviewping.coflo.domain.customprompt.controller.dto.response;

import lombok.Builder;

@Builder
public record CustomPromptResponse(Long customPromptId, String content) {}
