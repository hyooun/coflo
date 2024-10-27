package com.reviewping.coflo.domain.gitlab.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitlabMrDetailContent(
        Long id,
        Long iid,
        String title,
        String description,
        String state,
        LocalDateTime mergedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime closedAt,
        String sourceBranch,
        String targetBranch,
        List<String> labels,
        Boolean hasConflicts,
        String assigneeAvatarUrl,
        String reviewerAvatarUrl) {}
