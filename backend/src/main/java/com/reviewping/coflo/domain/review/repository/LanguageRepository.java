package com.reviewping.coflo.domain.review.repository;

import com.reviewping.coflo.domain.review.entity.Language;
import com.reviewping.coflo.domain.review.entity.LanguageType;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByType(LanguageType type);

    default Language getByType(LanguageType type) {
        return findByType(type)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNSUPPORTED_LANGUAGE));
    }
}
