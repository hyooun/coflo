package com.reviewping.coflo.domain.mergerequest.controller.dto.response;

import com.reviewping.coflo.global.client.gitlab.response.GitlabMrDetailContent;
import com.reviewping.coflo.global.client.gitlab.response.GitlabUserInfoContent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GitlabMrResponse(
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
        GitlabUserInfoContent assignee,
        GitlabUserInfoContent reviewer,
        Boolean isAiReviewCreated) {
    public static GitlabMrResponse of(
            GitlabMrDetailContent gitlabMrDetailContent, Boolean isAiReviewCreated) {
        return GitlabMrResponse.builder()
                .id(gitlabMrDetailContent.id())
                .iid(gitlabMrDetailContent.iid())
                .title(gitlabMrDetailContent.title())
                .description(gitlabMrDetailContent.description())
                .state(gitlabMrDetailContent.state())
                .mergedAt(
                        gitlabMrDetailContent.mergedAt() != null
                                ? gitlabMrDetailContent.mergedAt().toLocalDateTime()
                                : null)
                .createdAt(
                        gitlabMrDetailContent.createdAt() != null
                                ? gitlabMrDetailContent.createdAt().toLocalDateTime()
                                : null)
                .updatedAt(
                        gitlabMrDetailContent.updatedAt() != null
                                ? gitlabMrDetailContent.updatedAt().toLocalDateTime()
                                : null)
                .closedAt(
                        gitlabMrDetailContent.closedAt() != null
                                ? gitlabMrDetailContent.closedAt().toLocalDateTime()
                                : null)
                .sourceBranch(gitlabMrDetailContent.sourceBranch())
                .targetBranch(gitlabMrDetailContent.targetBranch())
                .labels(gitlabMrDetailContent.labels())
                .hasConflicts(gitlabMrDetailContent.hasConflicts())
                .assignee(gitlabMrDetailContent.assignee())
                .reviewer(
                        gitlabMrDetailContent.reviewers().isEmpty()
                                ? null
                                : gitlabMrDetailContent.reviewers().getFirst())
                .isAiReviewCreated(isAiReviewCreated)
                .build();
    }
}
