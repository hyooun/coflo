package com.reviewping.coflo.domain.userproject.repository;

import com.reviewping.coflo.domain.userproject.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {}
