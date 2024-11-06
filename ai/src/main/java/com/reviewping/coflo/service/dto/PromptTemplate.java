package com.reviewping.coflo.service.dto;

import java.time.LocalDateTime;

public record PromptTemplate(String content, String type, LocalDateTime createdDate) {}
