package com.uliga.uliga_backend.domain.Member.exception;

public class InvalidApplicationPasswordException extends IllegalArgumentException {

    public InvalidApplicationPasswordException() {
        super("잘못된 애플리케이션 비밀번호입니다");
    }

    public InvalidApplicationPasswordException(String s) {
        super(s);
    }
}
