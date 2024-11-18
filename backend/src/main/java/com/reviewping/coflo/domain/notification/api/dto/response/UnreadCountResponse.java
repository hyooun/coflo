package com.reviewping.coflo.domain.notification.api.dto.response;

public record UnreadCountResponse(Long unreadCount) {
    public static UnreadCountResponse of(Long unreadCount) {
        return new UnreadCountResponse(unreadCount);
    }
}
