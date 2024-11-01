package com.reviewping.coflo.domain.badge.entity;

import static com.reviewping.coflo.domain.badge.entity.BadgeType.*;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBadge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_code_id")
    private BadgeCode badgeCode;

    @Builder
    public UserBadge(User user, BadgeCode badgeCode) {
        this.user = user;
        this.badgeCode = badgeCode;
    }

    public static UserBadge of(User user, BadgeCode badgeCode) {
        return UserBadge.builder().user(user).badgeCode(badgeCode).build();
    }
}
