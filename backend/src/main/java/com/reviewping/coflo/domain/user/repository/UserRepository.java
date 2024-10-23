package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByOauth2Id(String oauth2Id);

    Optional<User> findByOauth2Id(String oauth2Id);
}
