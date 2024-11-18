package com.reviewping.coflo.domain.user.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate loginDate;

    public LoginHistory(Long userId, LocalDate loginDate) {
        this.userId = userId;
        this.loginDate = loginDate;
    }
}
