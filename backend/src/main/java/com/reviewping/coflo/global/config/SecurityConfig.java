package com.reviewping.coflo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.jwt.filter.JwtExceptionFilter;
import com.reviewping.coflo.global.jwt.filter.JwtVerifyFilter;
import com.reviewping.coflo.global.oauth.handler.CommonLoginFailHandler;
import com.reviewping.coflo.global.oauth.handler.CommonLoginSuccessHandler;
import com.reviewping.coflo.global.oauth.service.OAuth2UserService;
import com.reviewping.coflo.global.util.RedisUtil;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;
    private final OAuth2UserService oAuth2UserService;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommonLoginSuccessHandler commonLoginSuccessHandler() {
        return new CommonLoginSuccessHandler(userRepository, redisUtil);
    }

    @Bean
    public CommonLoginFailHandler commonLoginFailHandler() {
        return new CommonLoginFailHandler(objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtVerifyFilter jwtVerifyFilter = new JwtVerifyFilter(redisUtil);

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer -> {
                            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                                    SessionCreationPolicy.STATELESS);
                        })
                .cors(
                        cors -> {
                            cors.configurationSource(corsConfigurationSource());
                        })
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(objectMapper), jwtVerifyFilter.getClass())
                .formLogin(AbstractHttpConfigurer::disable);

        http.oauth2Login(
                httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer
                                .authorizationEndpoint(
                                        authorization ->
                                                authorization.baseUri("/api/oauth2/authorization"))
                                .redirectionEndpoint(
                                        redirection ->
                                                redirection.baseUri("/api/login/oauth2/code/*"))
                                .successHandler(commonLoginSuccessHandler())
                                .failureHandler(commonLoginFailHandler())
                                .userInfoEndpoint(
                                        userInfoEndpointConfig ->
                                                userInfoEndpointConfig.userService(
                                                        oAuth2UserService)));

        return http.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:5173", "https://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
