package com.reviewping.coflo.domain.badge.service;

import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeDetail;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeCodeRepository;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import java.util.ArrayList;
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
        List<UserBadge> userBadges = userBadgeRepository.findAllByUser(user);

        BadgeCode mainBadgeCode = user.getMainBadgeCode();
        Long mainbadgeCodeId = mainBadgeCode != null ? mainBadgeCode.getId() : null;
        List<BadgeDetail> badgeDetails = new ArrayList<>();

        for (UserBadge userBadge : userBadges) {
            badgeDetails.add(BadgeDetail.of(userBadge.getBadgeCode()));
        }

        return BadgeResponse.of(mainbadgeCodeId, badgeDetails);
    }

    @Transactional
    public void updateMainBadge(User user, Long badgeCodeId) {
        BadgeCode badgeCode = badgeCodeRepository.getById(badgeCodeId);
        userRepository.updateBadge(user, badgeCode);
    }

    @Transactional
    public void deleteMainBadge(User user) {
        userRepository.updateBadge(user, null);
    }
}
