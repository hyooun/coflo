package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
