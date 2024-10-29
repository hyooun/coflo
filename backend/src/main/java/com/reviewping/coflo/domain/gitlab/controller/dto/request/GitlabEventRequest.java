package com.reviewping.coflo.domain.gitlab.controller.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitlabEventRequest(
        String objectKind, String eventType, Project project, ObjectAttributes objectAttributes) {

    public record Project(Long id, String webUrl) {}

    public record ObjectAttributes(
            Long id,
            Long iid,
            String title,
            String createdAt,
            String updatedAt,
            String lastEditedAt,
            Boolean draft,
            String description,
            String action) {}
}
