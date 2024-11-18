package com.reviewping.coflo.global.auth.oauth.service;

import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.auth.oauth.model.UserDetails;
import com.reviewping.coflo.global.error.exception.BusinessException;
import io.jsonwebtoken.JwtException;
import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public Authentication getAuthentication(Long userId) {
        try {
            User user = userRepository.getById(userId);
            Set<SimpleGrantedAuthority> authorities =
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(new UserDetails(user), "", authorities);
        } catch (BusinessException e) {
            throw new JwtException(e.getErrorCode().getMessage());
        }
    }

    public void setAuthentication(Long userId) {
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(userId));
    }
}
