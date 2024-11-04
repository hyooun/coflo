package com.reviewping.coflo.domain.review.message;

import java.util.List;

public record ReviewRegenerateRequestMessage(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
