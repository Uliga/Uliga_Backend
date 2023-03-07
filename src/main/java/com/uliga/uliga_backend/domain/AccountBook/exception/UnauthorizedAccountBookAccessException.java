package com.uliga.uliga_backend.domain.AccountBook.exception;

public class UnauthorizedAccountBookAccessException extends IllegalArgumentException {
    public UnauthorizedAccountBookAccessException() {
        super("속하지 않은 가계부로 인해 조회가 불가합니다.");
    }

    public UnauthorizedAccountBookAccessException(String s) {
        super(s);
    }
}
