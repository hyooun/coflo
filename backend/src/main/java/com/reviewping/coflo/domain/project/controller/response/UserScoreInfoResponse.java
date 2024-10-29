package com.reviewping.coflo.domain.project.controller.response;

import java.util.List;

public record UserScoreInfoResponse(
        Long userId,
        String username,
        String profileImageUrl,
        String badgeName,
        String badgeImageUrl,
        List<ScoreResponse> scores) {}
