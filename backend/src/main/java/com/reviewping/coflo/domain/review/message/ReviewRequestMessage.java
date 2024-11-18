package com.reviewping.coflo.domain.review.message;

public record ReviewRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {
    public record MrContent(String mrDescription, String mrDiffs) {}
}
