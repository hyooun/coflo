package com.reviewping.coflo.service.dto;

import java.time.LocalDateTime;

public record PromptTemplate(Long id, String content, LocalDateTime createdDate) {
}