package com.reviewping.coflo.domain.project.controller.response;

import java.util.List;

public record CodeQualityScoreResponse(
        String codeQualityName, List<ScoreOfWeekResponse> scoreOfWeek) {}
