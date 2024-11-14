package com.reviewping.coflo.domain.notification.service;

import com.reviewping.coflo.domain.notification.api.dto.response.NotificationResponse;
import com.reviewping.coflo.domain.notification.api.dto.response.UnreadCountResponse;
import com.reviewping.coflo.domain.notification.entity.Notification;
import com.reviewping.coflo.domain.notification.repository.NotificationRepository;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SseService sseService;

    @Transactional
    public void create(Long userId, String content) {
        sseService.notify(userId, content);
        User user = userRepository.getById(userId);

        Notification notification =
                Notification.builder()
                        .user(user)
                        .content(content)
                        .targetUrl(null)
                        .isRead(false)
                        .build();
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getByUser(User user) {
        List<Notification> notifications =
                notificationRepository.findAllByUserOrderByCreatedDateDesc(user);

        return notifications.stream()
                .map(
                        notification -> {
                            return NotificationResponse.from(
                                    notification,
                                    formatCreatedDate(
                                            notification.getCreatedDate(), LocalDateTime.now()));
                        })
                .toList();
    }

    @Transactional
    public void updateIsRead(Long notificationId) {
        Notification notification = notificationRepository.getById(notificationId);
        if (!notification.isRead()) notification.updateIsRead(true);
    }

    public UnreadCountResponse unreadNotificationCount(User user) {
        List<Notification> notifications =
                notificationRepository.findAllByUserOrderByCreatedDateDesc(user);
        return UnreadCountResponse.of(
                notifications.stream().filter(notification -> !notification.isRead()).count());
    }

    private String formatCreatedDate(LocalDateTime startDT, LocalDateTime endDT) {
        long minutesBetween = ChronoUnit.MINUTES.between(startDT, endDT);

        if (minutesBetween < 1) {
            return "방금 전";
        } else if (minutesBetween < 60) {
            return minutesBetween + "분 전";
        }

        long hoursBetween = ChronoUnit.HOURS.between(startDT, endDT);
        if (hoursBetween < 24) {
            return hoursBetween + "시간 전";
        }

        return startDT.toLocalDate().toString().replace('-', '.');
    }
}
