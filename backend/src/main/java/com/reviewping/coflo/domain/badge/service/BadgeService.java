package com.reviewping.coflo.domain.badge.service;

import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeDetail;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeService {

    private final UserBadgeRepository userBadgeRepository;
    private final BadgeCodeRepository badgeCodeRepository;
    private final UserRepository userRepository;

    public BadgeResponse getBadgeInfo(User user) {
        BadgeCode mainBadgeCode = user.getMainBadgeCode();
        Long mainBadgeCodeId = mainBadgeCode != null ? mainBadgeCode.getId() : null;
        List<BadgeDetail> badgeDetails = getBadgeDetails(user);

        return BadgeResponse.of(mainBadgeCodeId, badgeDetails);
    }

    @Transactional
    public void updateMainBadge(User user, Long badgeCodeId) {
        BadgeCode badgeCode = null;
        if (badgeCodeId != null) {
            badgeCode = badgeCodeRepository.getById(badgeCodeId);
        }
        userRepository.updateBadge(user, badgeCode);
    }

    private List<BadgeDetail> getBadgeDetails(User user) {
        return badgeCodeRepository.findAll().stream()
                .map(
                        badgeCode ->
                                new BadgeDetail(
                                        badgeCode.getId(),
                                        badgeCode.getName(),
                                        badgeCode.getDescription(),
                                        badgeCode.getImageUrl(),
                                        userBadgeRepository.existsByUserAndBadgeCode(
                                                user, badgeCode)))
                .toList();
    }
}
