package com.reviewping.coflo.domain.review.message;

import com.reviewping.coflo.domain.review.message.ReviewRequestMessage.MrContent;
import java.util.List;

public record ReviewRegenerateRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
