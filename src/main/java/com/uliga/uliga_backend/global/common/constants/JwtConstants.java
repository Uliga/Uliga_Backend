package com.uliga.uliga_backend.global.common.constants;

public class JwtConstants {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_TYPE = "Bearer";
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 30;           // 하루 1000 * 60 * 60 * 24
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60;  // 7일 1000 * 60 * 60 * 24 * 7
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
}
