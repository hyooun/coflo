package com.reviewping.coflo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewping.coflo.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
