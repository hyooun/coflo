package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;

public record ScoreOfWeekResponse(Integer week, Long score) {
    public static ScoreOfWeekResponse of(UserProjectScore userProjectScore) {
        return new ScoreOfWeekResponse(
                userProjectScore.getWeek(), userProjectScore.getTotalScore());
    }
}
