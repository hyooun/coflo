package com.reviewping.coflo.domain.openai.dto.response;

public record ChatMessage(String role, String content, Object refusal) {}
