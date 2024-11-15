package com.reviewping.coflo.domain.review.message;

public record DetailedReviewRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        String diff,
        String baseSha,
        String headSha,
        String startSha,
        String newPath,
        String oldPath,
        String gitlabUrl) {}
