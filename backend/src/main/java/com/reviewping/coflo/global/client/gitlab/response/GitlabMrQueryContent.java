package com.reviewping.coflo.global.client.gitlab.response;

import java.time.OffsetDateTime;
import java.util.List;

public record GitlabMrQueryContent(
        String id,
        Long iid,
        String title,
        String description,
        String state,
        OffsetDateTime mergedAt,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String sourceBranch,
        String targetBranch,
        Labels labels,
        Assignees assignees,
        Reviewers reviewers) {

    public record Labels(List<LabelNode> nodes) {}

    public record LabelNode(String title, String color) {}

    public record Assignees(List<GitlabUserInfoContent> nodes) {}

    public record Reviewers(List<GitlabUserInfoContent> nodes) {}
}
