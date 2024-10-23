package com.reviewping.coflo.domain.userproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
}
