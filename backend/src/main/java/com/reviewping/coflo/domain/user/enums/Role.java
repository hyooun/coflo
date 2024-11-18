package com.reviewping.coflo.domain.user.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
