package com.reviewping.coflo.domain.review.message;

import com.reviewping.coflo.domain.review.message.ReviewRequestMessage.MrContent;

public record MrEvalRequestMessage(Long mrInfoId, Long branchId, MrContent mrContent) {}
