package com.reviewping.coflo.service.dto.response;

import java.util.List;

public record ReviewResponseMessage(
        String gitlabUrl,
        Long mrInfoId,
        Long userId,
        String content,
        List<RetrievalMessage> retrievals) {}
