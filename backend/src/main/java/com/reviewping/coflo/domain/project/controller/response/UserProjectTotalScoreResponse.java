package com.reviewping.coflo.domain.project.controller.response;

import java.time.LocalDate;
import java.util.List;

public record UserProjectTotalScoreResponse(
        LocalDate startDate, LocalDate endDate, List<ScoreOfWeekResponse> scoreOfWeek) {}
