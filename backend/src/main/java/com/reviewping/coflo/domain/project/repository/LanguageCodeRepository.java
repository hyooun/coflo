package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.LanguageCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LanguageCodeRepository extends JpaRepository<LanguageCode, Long> {
    Optional<LanguageCode> findByName(String name);

    @Query(
            "SELECT lc.color "
                    + "FROM LanguageCode lc "
                    + "WHERE lc.name = :name OR lc.name = 'Other' "
                    + "ORDER BY CASE WHEN lc.name = :name THEN 0 ELSE 1 END "
                    + "LIMIT 1")
    String findColorByNameOrDefault(@Param("name") String name);
}
