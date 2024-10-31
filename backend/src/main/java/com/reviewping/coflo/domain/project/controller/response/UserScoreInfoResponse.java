package com.reviewping.coflo.domain.project.controller.response;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.userproject.entity.UserProjectScore;
import java.util.List;
import lombok.Builder;

@Builder
public record UserScoreInfoResponse(
        Long userId,
        String username,
        String profileImageUrl,
        String badgeName,
        String badgeImageUrl,
        List<ScoreResponse> scores) {

    public static UserScoreInfoResponse of(
            User user, BadgeCode badgeCode, List<UserProjectScore> scores) {
        return UserScoreInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .profileImageUrl(user.getProfileImageUrl())
                .badgeName(badgeCode == null ? null : badgeCode.getName())
                .badgeImageUrl(badgeCode == null ? null : badgeCode.getImageUrl())
                .scores(scores.stream().map(ScoreResponse::of).toList())
                .build();
    }
}
