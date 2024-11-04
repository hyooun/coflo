package com.reviewping.coflo.domain.review.message;

import java.util.List;

import com.reviewping.coflo.domain.review.message.ReviewRequestMessage.MrContent;

public record ReviewRegenerateRequestMessage(
        Long projectId,
        Long mrInfoId,
        String branch,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
