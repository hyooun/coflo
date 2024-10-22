package com.reviewping.coflo.domain.user.enums;

import lombok.Getter;

@Getter
public enum Provider {

	GOOGLE("google"),
	KAKAO("kakao");

	private final String provider;

	Provider(String provider) {
		this.provider = provider;
	}
}
