package com.reviewping.coflo.service.dto.response;

import java.util.List;

public record DetailedReviewResponseMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        String content,
        String baseSha,
        String headSha,
        String startSha,
        String newPath,
        String oldPath,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
