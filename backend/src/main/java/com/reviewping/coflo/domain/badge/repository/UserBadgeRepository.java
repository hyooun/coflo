package com.reviewping.coflo.domain.badge.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    boolean existsByUserAndBadgeCode(User user, BadgeCode badgeCode);

    @Query(
            "SELECT u.id FROM User u WHERE u.id IN :userIds AND u.id NOT IN "
                    + "(SELECT ub.user.id FROM UserBadge ub WHERE ub.badgeCode.id = :badgeCodeId)")
    List<Long> findUserIdsWithoutBadge(
            @Param("userIds") List<Long> userIds, @Param("badgeCodeId") Long badgeCodeId);

    int countByUser(User user);

    default UserBadge getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(USER_BADGE_NOT_EXIST));
    }
}
