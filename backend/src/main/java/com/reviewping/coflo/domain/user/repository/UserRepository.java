package com.reviewping.coflo.domain.user.repository;

import static com.reviewping.coflo.global.error.ErrorCode.USER_NOT_EXIST;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByOauth2Id(String oauth2Id);

    default User getById(Long userId) {
        return findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_EXIST));
    }
}
