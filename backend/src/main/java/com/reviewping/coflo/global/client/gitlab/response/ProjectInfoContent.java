package com.reviewping.coflo.global.client.gitlab.response;

import java.util.Map;

public record ProjectInfoContent(
        int commitCount, Long branchCount, Long mergeRequestCount, Map<String, Double> languages) {

    public static ProjectInfoContent of(
            int commitCount,
            Long branchCount,
            Long mergeRequestCount,
            Map<String, Double> languages) {
        return new ProjectInfoContent(commitCount, branchCount, mergeRequestCount, languages);
    }
}
