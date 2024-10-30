package com.reviewping.coflo.domain.badge.repository;

import com.reviewping.coflo.domain.badge.entity.UserBadge;
import com.reviewping.coflo.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    @Query("SELECT ub FROM UserBadge ub WHERE ub.user = :user AND ub.isSelected = true")
    Optional<UserBadge> findSelectedBadgeByUser(@Param("user") User user);

    @EntityGraph(attributePaths = {"badge"})
    List<UserBadge> findAllByUser(User user);
}
