package com.reviewping.coflo.service.dto;

import java.util.List;

public record ReviewResponseMessage(
        String gitlabUrl, Long mrInfoId, String content, List<RetrievalResponse> retrievals) {
    private record RetrievalResponse(String content, String type) {}
}
