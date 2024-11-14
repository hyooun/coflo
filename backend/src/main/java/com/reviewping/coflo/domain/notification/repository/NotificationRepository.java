package com.reviewping.coflo.domain.notification.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.notification.entity.Notification;
import com.reviewping.coflo.domain.userproject.entity.UserProject;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserProjectOrderByCreatedDateDesc(UserProject userProject);

    default Notification getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(NOTIFICATION_NOT_EXIST));
    }
}
