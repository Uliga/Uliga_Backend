package com.uliga.uliga_backend.domain.Token.exception;

public class ExpireTokenException extends IllegalArgumentException {
    public ExpireTokenException() {
        super("만료된 토큰으로 온 인증 요청입니다");
    }

    public ExpireTokenException(String s) {
        super(s);
    }
}
