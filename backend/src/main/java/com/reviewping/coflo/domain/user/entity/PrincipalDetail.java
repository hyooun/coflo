package com.reviewping.coflo.domain.user.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class PrincipalDetail implements OAuth2User {

    private Long userId;

    public PrincipalDetail(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "name";
    }
}
