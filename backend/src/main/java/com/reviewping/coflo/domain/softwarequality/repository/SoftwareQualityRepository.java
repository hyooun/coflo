package com.reviewping.coflo.domain.softwarequality.repository;

import com.reviewping.coflo.domain.softwarequality.entity.SoftwareQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareQualityRepository extends JpaRepository<SoftwareQuality, Long> {}
