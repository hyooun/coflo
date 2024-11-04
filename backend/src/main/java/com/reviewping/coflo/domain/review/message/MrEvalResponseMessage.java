package com.reviewping.coflo.domain.review.message;

public record MrEvalResponseMessage(Long mrInfoId, MrEvaluationMessage mrEvaluationMessage) {
    public record MrEvaluationMessage(
            Integer readabilityScore,
            Integer consistencyScore,
            Integer reusabilityScore,
            Integer reliabilityScore,
            Integer securityScore,
            Integer maintainabilityScore) {}
}
