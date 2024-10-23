package com.reviewping.coflo.domain.badge.repository;

import com.reviewping.coflo.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
