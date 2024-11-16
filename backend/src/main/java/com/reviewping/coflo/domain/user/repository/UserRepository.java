package com.reviewping.coflo.domain.user.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauth2Id(String oauth2Id);

    @Modifying
    @Query("UPDATE User u SET u.mainBadgeCode = :badgeCode WHERE u = :user")
    void updateBadge(@Param("user") User user, @Param("badgeCode") BadgeCode badgeCode);

    @Query("SELECT u FROM User u WHERE u.username IN (:usernames)")
    List<User> findAllByUsernames(@Param("usernames") List<String> usernames);

    @Query("SELECT u FROM User u WHERE u.id IN (:ids)")
    List<User> findAllByIds(@Param("ids") List<Long> ids);

    default User getById(Long userId) {
        return findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
    }
}
