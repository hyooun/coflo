package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.global.client.gitlab.response.ProjectInfoContent;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectTeamDetailResponse(
        int commitCount,
        Long branchCount,
        Long mergeRequestCount,
        List<LanguageResponse> languages,
        Long aiReviewCount) {
    public static ProjectTeamDetailResponse of(
            ProjectInfoContent projectInfoContent,
            List<LanguageResponse> languages,
            Long aiReviewCount) {
        return ProjectTeamDetailResponse.builder()
                .commitCount(projectInfoContent.commitCount())
                .branchCount(projectInfoContent.branchCount())
                .mergeRequestCount(projectInfoContent.mergeRequestCount())
                .languages(languages)
                .aiReviewCount(aiReviewCount)
                .build();
    }
}
