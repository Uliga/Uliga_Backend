package com.uliga.uliga_backend.global.error.exception;

public class IdNotFoundException extends IllegalArgumentException {
    public IdNotFoundException() {
        super("아이디 값이 null이여서 존재하는 객체를 찾을 수 없습니다");
    }

    public IdNotFoundException(String s) {
        super(s);
    }
}
