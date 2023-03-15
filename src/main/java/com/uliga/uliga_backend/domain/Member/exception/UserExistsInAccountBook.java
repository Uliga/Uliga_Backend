package com.uliga.uliga_backend.domain.Member.exception;

public class UserExistsInAccountBook extends IllegalArgumentException {
    public UserExistsInAccountBook() {
        super("사용자가 이미 가계부에 존재합니다");
    }

    public UserExistsInAccountBook(String s) {
        super(s);
    }
}
