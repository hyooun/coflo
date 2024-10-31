package com.reviewping.coflo.domain.badge.service;

import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeEventService {

    private final UserRepository userRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;

    public void addFirstLogin(Long userId) {
        UserBadge userBadge =
                UserBadge.builder()
                        .user(userRepository.getById(userId))
                        .badgeCode(badgeCodeRepository.getById(1L))
                        .build();

        userBadgeRepository.save(userBadge);
    }
}
