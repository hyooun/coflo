package com.reviewping.coflo.domain.review.repository;

import com.reviewping.coflo.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {}
