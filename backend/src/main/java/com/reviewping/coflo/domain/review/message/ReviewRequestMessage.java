package com.reviewping.coflo.domain.review.message;

public record ReviewRequestMessage(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl) {}
