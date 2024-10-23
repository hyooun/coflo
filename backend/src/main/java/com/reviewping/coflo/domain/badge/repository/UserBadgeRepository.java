package com.reviewping.coflo.domain.badge.repository;

import com.reviewping.coflo.domain.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {}
