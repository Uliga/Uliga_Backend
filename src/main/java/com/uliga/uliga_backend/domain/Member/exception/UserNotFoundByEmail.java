package com.uliga.uliga_backend.domain.Member.exception;

public class UserNotFoundByEmail extends IllegalArgumentException {
    public UserNotFoundByEmail() {
        super("해당 이메일로 존재하는 유저를 찾을 수 없습니다");
    }

    public UserNotFoundByEmail(String s) {
        super(s);
    }
}

