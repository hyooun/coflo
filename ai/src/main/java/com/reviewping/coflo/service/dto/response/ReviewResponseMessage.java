package com.reviewping.coflo.service.dto.response;

import java.util.List;

public record ReviewResponseMessage(
        String gitlabUrl, Long mrInfoId, String content, List<RetrievalMessage> retrievals) {}
