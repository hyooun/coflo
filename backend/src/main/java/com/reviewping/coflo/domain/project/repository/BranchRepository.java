package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.domain.project.entity.Project;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByNameAndProject(String name, Project project);

    default Branch getByNameAndProject(String name, Project project) {
        return findByNameAndProject(name, project)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRANCH_NOT_FOUND));
    }
}
