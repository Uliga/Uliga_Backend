package com.uliga.uliga_backend.domain.member.exception;

public class CannotLoginException extends IllegalArgumentException {
    public CannotLoginException() {
        super("잘못된 이메일, 비밀번호 입니다.");
    }

    public CannotLoginException(String s) {
        super(s);
    }
}
