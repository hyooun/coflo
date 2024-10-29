package com.reviewping.coflo.global.client.gitlab.response;

import java.util.Map;

public record ProjectInfoContent(
        Long commitCount,
        Long branchCount,
        Long mergerRequestCount,
        Map<String, Double> languages) {

    public static ProjectInfoContent of(
            Long commitCount,
            Long branchCount,
            Long mergerRequestCount,
            Map<String, Double> languages) {
        return new ProjectInfoContent(commitCount, branchCount, mergerRequestCount, languages);
    }
}
