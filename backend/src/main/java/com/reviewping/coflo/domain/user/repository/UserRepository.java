package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByOauth2Id(String oauth2Id);

    Optional<User> findByOauth2Id(String oauth2Id);

    default User findUserById(Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_EXIST));
    }
}
