package com.reviewping.coflo.domain.notification.api;

import com.reviewping.coflo.domain.notification.api.dto.response.NotificationResponse;
import com.reviewping.coflo.domain.notification.api.dto.response.UnreadCountResponse;
import com.reviewping.coflo.domain.notification.service.NotificationService;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.auth.AuthUser;
import com.reviewping.coflo.global.common.response.ApiResponse;
import com.reviewping.coflo.global.common.response.impl.ApiSuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/notifications")
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회", description = "자신의 알림 목록을 조회합니다.")
    @GetMapping
    public ApiResponse<List<NotificationResponse>> findNotification(
            @AuthUser User user, @RequestParam(name = "projectId") Long projectId) {
        return ApiSuccessResponse.success(notificationService.getByUser(user.getId(), projectId));
    }

    @Operation(summary = "읽지 않은 알림 개수 조회", description = "읽지 않은 알림 개수를 조회합니다.")
    @GetMapping("/unread-counts")
    public ApiResponse<UnreadCountResponse> findUnreadNotificationsCount(
            @AuthUser User user, @RequestParam(name = "projectId") Long projectId) {
        return ApiSuccessResponse.success(
                notificationService.unreadNotificationCount(user.getId(), projectId));
    }

    @Operation(summary = "알림 확인", description = "자신의 알림을 확인합니다.")
    @PatchMapping("/{notificationId}")
    public ApiResponse<Void> updateNotification(
            @PathVariable(name = "notificationId") Long notificationId) {
        notificationService.updateIsRead(notificationId);
        return ApiSuccessResponse.success();
    }
}
