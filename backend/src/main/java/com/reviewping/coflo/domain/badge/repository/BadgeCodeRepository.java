package com.reviewping.coflo.domain.badge.repository;

import static com.reviewping.coflo.global.error.ErrorCode.BADGE_NOT_EXIST;

import com.reviewping.coflo.domain.badge.entity.BadgeCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeCodeRepository extends JpaRepository<BadgeCode, Long> {
    default BadgeCode getById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(BADGE_NOT_EXIST));
    }
}
