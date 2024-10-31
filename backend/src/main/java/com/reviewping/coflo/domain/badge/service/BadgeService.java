package com.reviewping.coflo.domain.badge.service;

import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeDetail;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.entity.Badge;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.BadgeRepository;
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
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    public BadgeResponse getBadgeInfo(User user) {
        List<UserBadge> userBadges = userBadgeRepository.findAllByUser(user);

        Badge mainBadge = user.getBadge();
        Long mainBadgeId = mainBadge != null ? mainBadge.getId() : null;
        List<BadgeDetail> badgeDetails = new ArrayList<>();

        for (UserBadge userBadge : userBadges) {
            badgeDetails.add(BadgeDetail.of(userBadge.getBadge()));
        }

        return BadgeResponse.of(mainBadgeId, badgeDetails);
    }

    @Transactional
    public void updateMainBadge(User user, Long badgeId) {
        Badge badge = badgeRepository.getById(badgeId);
        userRepository.updateBadge(user, badge);
    }
}
