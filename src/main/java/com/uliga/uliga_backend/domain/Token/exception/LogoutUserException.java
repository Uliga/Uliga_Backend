package com.uliga.uliga_backend.domain.Token.exception;

public class LogoutUserException extends IllegalArgumentException {
    public LogoutUserException() {
        super("로그아웃한 사용자입니다. 재로그인을 해야합니다");
    }

    public LogoutUserException(String s) {
        super(s);
    }
}
