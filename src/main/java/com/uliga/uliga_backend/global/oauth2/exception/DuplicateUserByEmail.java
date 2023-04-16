package com.uliga.uliga_backend.global.oauth2.exception;

public class DuplicateUserByEmail extends IllegalArgumentException {
    public DuplicateUserByEmail() {
        super("이메일로 회원가입되어있습니다. 해당 이메일로 로그인 진행해 주세요.");
    }

    public DuplicateUserByEmail(String s) {
        super(s);
    }
}
