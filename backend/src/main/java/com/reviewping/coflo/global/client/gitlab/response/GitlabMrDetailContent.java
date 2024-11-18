package com.reviewping.coflo.global.client.gitlab.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitlabMrDetailContent(
        Long id,
        Long iid,
        String title,
        String description,
        String state,
        OffsetDateTime mergedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime closedAt,
        String sourceBranch,
        String targetBranch,
        List<String> labels,
        Boolean hasConflicts,
        GitlabUserInfoSnakeContent assignee,
        List<GitlabUserInfoSnakeContent> reviewers) {
    public record GitlabUserInfoSnakeContent(
            String username, String name, @JsonProperty("avatar_url") String avatarUrl) {}
}
