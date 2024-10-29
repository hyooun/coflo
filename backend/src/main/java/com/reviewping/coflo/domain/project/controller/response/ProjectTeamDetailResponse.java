package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.global.client.gitlab.response.ProjectInfoContent;
import java.util.Map;
import lombok.Builder;

@Builder
public record ProjectTeamDetailResponse(
        int commitCount,
        Long branchCount,
        Long mergerRequestCount,
        Map<String, Double> languages,
        Long aiReviewCount) {
    public static ProjectTeamDetailResponse of(
            ProjectInfoContent projectInfoContent, Long aiReviewCount) {
        return ProjectTeamDetailResponse.builder()
                .commitCount(projectInfoContent.commitCount())
                .branchCount(projectInfoContent.branchCount())
                .mergerRequestCount(projectInfoContent.mergerRequestCount())
                .languages(projectInfoContent.languages())
                .aiReviewCount(aiReviewCount)
                .build();
    }
}
