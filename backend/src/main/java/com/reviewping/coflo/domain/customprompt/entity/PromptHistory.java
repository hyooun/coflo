package com.reviewping.coflo.domain.customprompt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromptHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime updateDateTime;

    public PromptHistory(Long userId, LocalDateTime updateDateTime) {
        this.userId = userId;
        this.updateDateTime = updateDateTime;
    }
}
