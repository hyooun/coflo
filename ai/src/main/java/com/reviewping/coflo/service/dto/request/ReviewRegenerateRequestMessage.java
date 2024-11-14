package com.reviewping.coflo.service.dto.request;

import com.reviewping.coflo.service.dto.request.ReviewRequestMessage.MrContent;
import com.reviewping.coflo.service.dto.response.RetrievalMessage;
import java.util.List;

public record ReviewRegenerateRequestMessage(
        Long projectId,
        Long mrInfoId,
        Long branchId,
        Long userId,
        MrContent mrContent,
        String customPrompt,
        String gitlabUrl,
        List<RetrievalMessage> retrievals) {}
