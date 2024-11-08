package com.reviewping.coflo.global.client.gitlab.response;

import com.reviewping.coflo.domain.project.controller.response.LanguageResponse;
import java.util.List;

public record ProjectInfoContent(
        int commitCount,
        Long branchCount,
        Long mergeRequestCount,
        List<LanguageResponse> languages) {

    public static ProjectInfoContent of(
            int commitCount,
            Long branchCount,
            Long mergeRequestCount,
            List<LanguageResponse> languages) {
        return new ProjectInfoContent(commitCount, branchCount, mergeRequestCount, languages);
    }
}
