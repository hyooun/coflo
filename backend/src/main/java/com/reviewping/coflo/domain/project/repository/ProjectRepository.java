package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
