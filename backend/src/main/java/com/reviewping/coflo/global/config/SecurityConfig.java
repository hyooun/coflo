package com.reviewping.coflo.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reviewping.coflo.domain.badge.service.BadgeEventService;
import com.reviewping.coflo.domain.user.service.LoginHistoryService;
import com.reviewping.coflo.global.auth.jwt.filter.JwtExceptionFilter;
import com.reviewping.coflo.global.auth.jwt.filter.JwtVerifyFilter;
import com.reviewping.coflo.global.auth.jwt.handler.JwtAccessDeniedHandler;
import com.reviewping.coflo.global.auth.jwt.handler.JwtAuthenticationEntryPoint;
import com.reviewping.coflo.global.auth.oauth.handler.CommonLoginFailHandler;
import com.reviewping.coflo.global.auth.oauth.handler.CommonLoginSuccessHandler;
import com.reviewping.coflo.global.auth.oauth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.reviewping.coflo.global.auth.oauth.service.AuthenticationService;
import com.reviewping.coflo.global.auth.oauth.service.OAuth2UserService;
import com.reviewping.coflo.global.util.CookieUtil;
import com.reviewping.coflo.global.util.RedisUtil;
import com.reviewping.coflo.global.util.WebHookUtil;
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
    private final CookieUtil cookieUtil;
    private final WebHookUtil webHookUtil;
    private final ObjectMapper objectMapper;
    private final OAuth2UserService oAuth2UserService;
    private final AuthenticationService authenticationService;
    private final LoginHistoryService loginHistoryService;
    private final BadgeEventService badgeEventService;
    private final HttpCookieOAuth2AuthorizationRequestRepository
            httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommonLoginSuccessHandler commonLoginSuccessHandler() {
        return new CommonLoginSuccessHandler(
                redisUtil, cookieUtil, loginHistoryService, badgeEventService);
    }

    @Bean
    public CommonLoginFailHandler commonLoginFailHandler() {
        return new CommonLoginFailHandler(objectMapper, webHookUtil);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtVerifyFilter jwtVerifyFilter =
                new JwtVerifyFilter(redisUtil, cookieUtil, authenticationService);

        http.exceptionHandling(
                (exceptions) ->
                        exceptions
                                .authenticationEntryPoint(
                                        new JwtAuthenticationEntryPoint()) // 인증 실패 핸들링
                                .accessDeniedHandler(new JwtAccessDeniedHandler())); // 인가 실패 핸들링

        http.authorizeHttpRequests(
                authorize ->
                        authorize
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/actuator/**",
                                        "/test",
                                        "/favicon.ico",
                                        "/webhook/*")
                                .permitAll()
                                .anyRequest()
                                .authenticated() // 그 외 모든 경로는 인증 필요
                );

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
                                                authorization
                                                        .baseUri("/api/oauth2/authorization")
                                                        .authorizationRequestRepository(
                                                                httpCookieOAuth2AuthorizationRequestRepository))
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
