package com.reviewping.coflo.domain.badge.repository;

import static com.reviewping.coflo.global.error.ErrorCode.*;

import com.reviewping.coflo.domain.badge.entity.Badge;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    default Badge getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(BADGE_NOT_EXIST));
    }
}
