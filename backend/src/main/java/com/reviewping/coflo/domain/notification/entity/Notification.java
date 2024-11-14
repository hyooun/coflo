package com.reviewping.coflo.domain.notification.entity;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String content;

    private String targetUrl;

    private boolean isRead;

    @Builder
    public Notification(User user, String content, String targetUrl, boolean isRead) {
        this.user = user;
        this.content = content;
        this.targetUrl = targetUrl;
        this.isRead = isRead;
    }

    public void updateIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
