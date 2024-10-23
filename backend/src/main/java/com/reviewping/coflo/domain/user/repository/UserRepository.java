package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByOauth2Id(String oauth2Id);

    Optional<User> findByOauth2Id(String oauth2Id);
}
