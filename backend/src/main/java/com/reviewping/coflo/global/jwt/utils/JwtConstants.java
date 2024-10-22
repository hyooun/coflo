package com.reviewping.coflo.global.jwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstants {
	public static String key;

	public static int ACCESS_EXP_TIME;

	public static int REFRESH_EXP_TIME;

	public static String JWT_HEADER;

	public static String JWT_REFRESH_HEADER;

	public static final String JWT_TYPE = "Bearer ";

	@Value("${jwt.secretKey}")
	public void setKey(String secretKey) {
		this.key = secretKey;
	}

	@Value("${jwt.access.expiration}")
	public void setAccessExpTime(int expiration) {
		this.ACCESS_EXP_TIME = expiration;
	}

	@Value("${jwt.refresh.expiration}")
	public void setRefreshExpTime(int expiration) {
		this.REFRESH_EXP_TIME = expiration;
	}

	@Value("${jwt.access.header}")
	public void setJwtHeader(String header) {
		this.JWT_HEADER = header;
	}

	@Value("${jwt.refresh.header}")
	public void setJwtRefreshHeader(String header) {
		this.JWT_REFRESH_HEADER = header;
	}
}
