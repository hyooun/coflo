package com.reviewping.coflo.domain.user.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;

import com.reviewping.coflo.domain.badge.entity.Badge;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauth2Id(String oauth2Id);

    @Modifying
    @Query("UPDATE User u SET u.mainBadge = :badge WHERE u = :user")
    void updateBadge(@Param("user") User user, @Param("badge") Badge badge);

    default User getById(Long userId) {
        return findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
    }
}
