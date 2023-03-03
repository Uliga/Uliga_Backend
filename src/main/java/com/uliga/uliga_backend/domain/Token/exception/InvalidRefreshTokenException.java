package com.uliga.uliga_backend.domain.Token.exception;

public class InvalidRefreshTokenException extends IllegalArgumentException {
    public InvalidRefreshTokenException() {
        super("유효하지 않은 리프레쉬 토큰 입니다");
    }

    public InvalidRefreshTokenException(String s) {
        super(s);
    }
}
