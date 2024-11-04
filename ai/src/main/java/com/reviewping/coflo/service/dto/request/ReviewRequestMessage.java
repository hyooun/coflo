package com.reviewping.coflo.service.dto.request;

public record ReviewRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {
    public record MrContent(String mrDescription, String mrDiffs) {}
}
