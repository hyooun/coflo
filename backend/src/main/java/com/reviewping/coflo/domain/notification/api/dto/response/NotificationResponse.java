package com.reviewping.coflo.domain.notification.api.dto.response;

import com.reviewping.coflo.domain.notification.entity.Notification;

public record NotificationResponse(
        Long id, String content, String targetUrl, boolean isRead, String createdDate) {
    public static NotificationResponse from(Notification notification, String createdDate) {
        return new NotificationResponse(
                notification.getId(),
                notification.getContent(),
                notification.getTargetUrl(),
                notification.isRead(),
                createdDate);
    }
}
