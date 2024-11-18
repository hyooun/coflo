package com.reviewping.coflo.global.auth.oauth.model;

import com.reviewping.coflo.domain.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
@RequiredArgsConstructor
public class UserDetails implements OAuth2User {

    private final User user;

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
        String username = user.getUsername();
        return username == null ? "empty" : username;
    }
}
