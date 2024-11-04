package com.reviewping.coflo.service.dto.request;

import com.reviewping.coflo.service.dto.request.ReviewRequestMessage.MrContent;

public record MrEvalRequestMessage(Long mrInfoId, Long branchId, MrContent mrContent) {}
