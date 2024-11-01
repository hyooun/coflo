package com.reviewping.coflo.domain.badge.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    @EntityGraph(attributePaths = {"badgeCode"})
    List<UserBadge> findAllByUser(User user);

    UserBadge findByUserAndBadgeCode(User user, BadgeCode badgeCode);

    default UserBadge getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(USER_BADGE_NOT_EXIST));
    }
}
