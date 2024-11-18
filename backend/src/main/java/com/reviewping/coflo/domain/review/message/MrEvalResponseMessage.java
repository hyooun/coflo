package com.reviewping.coflo.domain.review.message;

public record MrEvalResponseMessage(
        Long mrInfoId, MrEvaluationMessage mrEvaluationMessage, String username) {
    public record MrEvaluationMessage(
            Integer readabilityScore,
            Integer consistencyScore,
            Integer reusabilityScore,
            Integer reliabilityScore,
            Integer securityScore,
            Integer maintainabilityScore) {}
}
