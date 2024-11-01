package com.reviewping.coflo.domain.user.service;

import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.entity.LoginHistory;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.LoginHistoryRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;
    private final BadgeEventService badgeEventService;

    @Transactional
    public void recordLogin(User user) {
        LocalDate today = LocalDate.now();

        if (!loginHistoryRepository.existsByUserIdAndLoginDate(user.getId(), today)) {
            loginHistoryRepository.save(new LoginHistory(user.getId(), today));
            badgeEventService.eventLoginCount(user);
        }
    }
}
