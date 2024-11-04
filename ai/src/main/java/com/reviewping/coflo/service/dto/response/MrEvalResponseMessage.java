package com.reviewping.coflo.service.dto.response;

public record MrEvalResponseMessage(Long mrInfoId, MrEvaluationMessage mrEvaluationMessage) {
    public record MrEvaluationMessage(
            Integer readabilityScore,
            Integer consistencyScore,
            Integer reusabilityScore,
            Integer reliabilityScore,
            Integer securityScore,
            Integer maintainabilityScore) {}
}
