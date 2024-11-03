package com.reviewping.coflo.domain.review.message;

public record ReviewRequest(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {}
