package com.reviewping.coflo.global.auth.oauth.model;

import java.util.Map;
import lombok.Getter;

@Getter
public class OAuth2UserInfo {
    private String oauthId;
    private String provider;

    public OAuth2UserInfo(
            Map<String, Object> attributes, String userNameAttributeName, String provider) {
        this.oauthId = String.valueOf(attributes.get(userNameAttributeName));
        this.provider = provider;
    }
}
