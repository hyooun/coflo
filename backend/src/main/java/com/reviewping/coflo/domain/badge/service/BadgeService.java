package com.reviewping.coflo.domain.badge.service;

import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeDetail;
import com.reviewping.coflo.domain.badge.controller.dto.response.BadgeResponse;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.badge.repository.UserBadgeRepository;
import com.reviewping.coflo.domain.user.entity.User;
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

    public BadgeResponse getBadgeInfo(User user) {
        List<UserBadge> userBadges = userBadgeRepository.findAllByUser(user);

        Long mainBadgeId = null;
        List<BadgeDetail> badgeDetails = new ArrayList<>();

        for (UserBadge userBadge : userBadges) {
            badgeDetails.add(BadgeDetail.of(userBadge.getId(), userBadge.getBadge()));
            if (userBadge.isSelected()) {
                mainBadgeId = userBadge.getId();
            }
        }

        return BadgeResponse.of(mainBadgeId, badgeDetails);
    }

    @Transactional
    public void updateMainBadge(User user, Long badgeId) {
        userBadgeRepository
                .findSelectedBadgeByUser(user)
                .ifPresent(oldMainBadge -> oldMainBadge.updateIsSelected(false));

        if (badgeId != null) {
            UserBadge newMainBadge = userBadgeRepository.getById(badgeId);
            newMainBadge.updateIsSelected(true);
        }
    }
}
