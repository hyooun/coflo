package com.reviewping.coflo.domain.mergerequest.controller.dto.response;

import com.reviewping.coflo.domain.gitlab.dto.response.GitlabMrDetailContent;
import com.reviewping.coflo.domain.gitlab.dto.response.GitlabUserInfoContent;
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
                .mergedAt(gitlabMrDetailContent.mergedAt().toLocalDateTime())
                .createdAt(gitlabMrDetailContent.createdAt().toLocalDateTime())
                .updatedAt(gitlabMrDetailContent.updatedAt().toLocalDateTime())
                .closedAt(gitlabMrDetailContent.closedAt().toLocalDateTime())
                .sourceBranch(gitlabMrDetailContent.sourceBranch())
                .targetBranch(gitlabMrDetailContent.targetBranch())
                .labels(gitlabMrDetailContent.labels())
                .hasConflicts(gitlabMrDetailContent.hasConflicts())
                .assignee(gitlabMrDetailContent.assignee())
                .reviewer(gitlabMrDetailContent.reviewer())
                .isAiReviewCreated(isAiReviewCreated)
                .build();
    }
}
