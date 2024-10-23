package com.reviewping.coflo.domain.user.repository;

import com.reviewping.coflo.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByOauth2Id(String oauth2Id);

    Optional<User> findByOauth2Id(String oauth2Id);
}
