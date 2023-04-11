package com.uliga.uliga_backend.domain.Token.exception;

public class ExpireAccessTokenException extends IllegalArgumentException {
    public ExpireAccessTokenException() {
        super("만료된 엑세스 토큰으로 온 인증 요청입니다");
    }

    public ExpireAccessTokenException(String s) {
        super(s);
    }
}
