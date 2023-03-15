package com.uliga.uliga_backend.global.error.exception;

public class InvalidDataValueException extends IllegalArgumentException {
    public InvalidDataValueException() {
        super("잘못된 값으로 서버 내부에 오류가 발생하였습니다");
    }

    public InvalidDataValueException(String s) {
        super(s);
    }
}
