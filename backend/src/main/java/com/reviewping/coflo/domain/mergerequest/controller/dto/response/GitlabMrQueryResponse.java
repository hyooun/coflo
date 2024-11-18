package com.reviewping.coflo.domain.mergerequest.controller.dto.response;

import com.reviewping.coflo.global.client.gitlab.response.GitlabMrQueryContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabMrQueryContent.Labels;
import com.reviewping.coflo.global.client.gitlab.response.GitlabUserInfoContent;
import com.reviewping.coflo.global.util.GraphQlUtil;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record GitlabMrQueryResponse(
        Long id,
        Long iid,
        String title,
        String description,
        String state,
        LocalDateTime mergedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String sourceBranch,
        String targetBranch,
        Labels labels,
        GitlabUserInfoContent assignee,
        GitlabUserInfoContent reviewer,
        Boolean isAiReviewCreated) {
    public static GitlabMrQueryResponse of(
            GitlabMrQueryContent gitlabMrQueryContent, Boolean isAiReviewCreated) {
        return GitlabMrQueryResponse.builder()
                .id(GraphQlUtil.extractIdFromId(gitlabMrQueryContent.id()))
                .iid(gitlabMrQueryContent.iid())
                .title(gitlabMrQueryContent.title())
                .description(gitlabMrQueryContent.description())
                .state(gitlabMrQueryContent.state())
                .mergedAt(
                        gitlabMrQueryContent.mergedAt() != null
                                ? gitlabMrQueryContent.mergedAt().toLocalDateTime()
                                : null)
                .createdAt(
                        gitlabMrQueryContent.createdAt() != null
                                ? gitlabMrQueryContent.createdAt().toLocalDateTime()
                                : null)
                .updatedAt(
                        gitlabMrQueryContent.updatedAt() != null
                                ? gitlabMrQueryContent.updatedAt().toLocalDateTime()
                                : null)
                .sourceBranch(gitlabMrQueryContent.sourceBranch())
                .targetBranch(gitlabMrQueryContent.targetBranch())
                .labels(gitlabMrQueryContent.labels())
                .assignee(
                        gitlabMrQueryContent.assignees().nodes().isEmpty()
                                ? null
                                : gitlabMrQueryContent.assignees().nodes().getFirst())
                .reviewer(
                        gitlabMrQueryContent.reviewers().nodes().isEmpty()
                                ? null
                                : gitlabMrQueryContent.reviewers().nodes().getFirst())
                .isAiReviewCreated(isAiReviewCreated)
                .build();
    }
}
