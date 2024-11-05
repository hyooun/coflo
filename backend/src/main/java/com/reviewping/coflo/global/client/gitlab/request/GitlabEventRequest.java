package com.reviewping.coflo.global.client.gitlab.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;

/**
 * @see <a href="https://archives.docs.gitlab.com/17.0/ee/user/project/integrations/webhook_events.html#merge-request-events">merge-request-events</a>
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GitlabEventRequest(
        String objectKind, String eventType, Project project, ObjectAttributes objectAttributes) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Project(Long id, String webUrl) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record ObjectAttributes(
            Long id,
            Long iid,
            String targetBranch,
            String title,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            Boolean draft,
            String description,
            String action) {}
}
