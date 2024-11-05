package com.reviewping.coflo.domain.project.repository;

import com.reviewping.coflo.domain.project.entity.Branch;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByName(String name);

    default Branch getByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.BRANCH_NOT_FOUND));
    }
}
