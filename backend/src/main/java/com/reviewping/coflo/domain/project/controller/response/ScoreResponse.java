package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;

public record ScoreResponse(String name, Long score) {

    public static ScoreResponse of(UserProjectScore userProjectScore) {
        return new ScoreResponse(
                userProjectScore.getCodeQualityCode().getName(), userProjectScore.getTotalScore());
    }
}
