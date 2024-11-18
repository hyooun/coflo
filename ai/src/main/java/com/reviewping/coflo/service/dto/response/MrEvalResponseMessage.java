package com.reviewping.coflo.service.dto.response;

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
