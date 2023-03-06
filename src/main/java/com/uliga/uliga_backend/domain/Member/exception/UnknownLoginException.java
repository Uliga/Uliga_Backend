package com.uliga.uliga_backend.domain.Member.exception;

public class UnknownLoginException extends IllegalArgumentException {
    public UnknownLoginException() {
        super("알 수 없는 방법으로 온 로그인 요청입니다");
    }

    public UnknownLoginException(String s) {
        super(s);
    }
}
