package com.reviewping.coflo.domain.review.message;

import java.util.List;

public record ReviewResponseMessage(
        Long userId,
        String gitlabUrl,
        Long mrInfoId,
        String content,
        List<RetrievalMessage> retrievals) {}
