package com.reviewping.coflo.domain.project.controller.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record ProjectTeamRewardResponse(
        LocalDate startDate, LocalDate endDate, List<UserScoreInfoResponse> userScores) {
    public static ProjectTeamRewardResponse of(
            LocalDate[] startAndEndDates, List<UserScoreInfoResponse> userScores) {
        return ProjectTeamRewardResponse.builder()
                .startDate(startAndEndDates[0])
                .endDate(startAndEndDates[1])
                .userScores(userScores)
                .build();
    }
}
