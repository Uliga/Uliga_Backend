package com.uliga.uliga_backend.domain.Token.exception;

public class EmptyTokenException extends IllegalArgumentException {
    public EmptyTokenException() {
        super("헤더에 토큰이 없습니다. 토큰을 담아서 인증을 진행해주세요");
    }

    public EmptyTokenException(String s) {
        super(s);
    }
}
