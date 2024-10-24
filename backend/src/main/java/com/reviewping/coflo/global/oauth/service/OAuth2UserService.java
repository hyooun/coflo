package com.reviewping.coflo.global.oauth.service;

import com.reviewping.coflo.domain.user.entity.PrincipalDetail;
import com.reviewping.coflo.domain.user.entity.User;
import com.reviewping.coflo.domain.user.enums.Provider;
import com.reviewping.coflo.domain.user.enums.Role;
import com.reviewping.coflo.domain.user.repository.UserRepository;
import com.reviewping.coflo.global.oauth.user.OAuth2UserInfo;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("=== OAuth2UserService 실행 ===");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String userNameAttributeName =
                userRequest
                        .getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo =
                new OAuth2UserInfo(attributes, userNameAttributeName, provider);

        Optional<User> bySocialId = userRepository.findByOauth2Id(oAuth2UserInfo.getOauthId());
        User user =
                bySocialId.orElseGet(
                        () ->
                                saveSocialMember(
                                        oAuth2UserInfo.getOauthId(),
                                        Provider.valueOf(provider.toUpperCase())));

        return new PrincipalDetail(user.getId());
    }

    public User saveSocialMember(String oauthId, Provider provider) {
        log.info("=== 새로운 소셜 로그인 사용자 추가 ===");
        User newUser = User.builder().oauth2Id(oauthId).provider(provider).role(Role.USER).build();
        return userRepository.save(newUser);
    }
}
