package com.reviewping.coflo.global.auth.jwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstants {
    public static String key;

    public static int ACCESS_EXP_TIME;

    public static int REFRESH_EXP_TIME;

    public static String ACCESS_NAME;

    public static String REFRESH_NAME;

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

    @Value("${jwt.access.name}")
    public void setAccessName(String name) {
        this.ACCESS_NAME = name;
    }

    @Value("${jwt.refresh.name}")
    public void setRefreshName(String name) {
        this.REFRESH_NAME = name;
    }
}
