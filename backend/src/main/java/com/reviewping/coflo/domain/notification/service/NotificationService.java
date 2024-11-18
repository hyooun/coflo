package com.reviewping.coflo.domain.notification.service;

import com.reviewping.coflo.domain.notification.api.dto.response.NotificationResponse;
import com.reviewping.coflo.domain.notification.api.dto.response.UnreadCountResponse;
import com.reviewping.coflo.domain.notification.entity.Notification;
import com.reviewping.coflo.domain.notification.repository.NotificationRepository;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.domain.project.repository.ProjectRepository;
import com.reviewping.coflo.domain.user.entity.GitlabAccount;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.domain.userproject.repository.UserProjectRepository;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
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

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final UserProjectRepository userProjectRepository;
    private final SseService sseService;
    private final ProjectRepository projectRepository;

    @Transactional
    public void create(Long userId, UserProject userProject, String content) {
        sseService.notify(userId, content);

        Notification notification =
                Notification.builder()
                        .userProject(userProject)
                        .content(content)
                        .targetUrl(null)
                        .isRead(false)
                        .build();
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getByUser(Long userId, Long projectId) {
        UserProject userProject = getUserProject(userId, projectId);

        List<Notification> notifications =
                notificationRepository.findAllByUserProjectOrderByCreatedDateDesc(userProject);

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

    public UnreadCountResponse unreadNotificationCount(Long userId, Long projectId) {
        UserProject userProject = getUserProject(userId, projectId);

        List<Notification> notifications =
                notificationRepository.findAllByUserProjectOrderByCreatedDateDesc(userProject);
        return UnreadCountResponse.of(
                notifications.stream().filter(notification -> !notification.isRead()).count());
    }

    public UserProject getUserProject(Long userId, Long projectId) {
        User user = userRepository.getById(userId);
        GitlabAccount gitlabAccount = user.getGitlabAccounts().getFirst();
        Project project = projectRepository.getById(projectId);
        UserProject userProject =
                userProjectRepository
                        .findByProjectAndGitlabAccount(project, gitlabAccount)
                        .orElseThrow(() -> new BusinessException(ErrorCode.USER_PROJECT_NOT_EXIST));
        return userProject;
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
