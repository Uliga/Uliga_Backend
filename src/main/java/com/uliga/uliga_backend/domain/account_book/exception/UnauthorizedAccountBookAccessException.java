package com.uliga.uliga_backend.domain.account_book.exception;

public class UnauthorizedAccountBookAccessException extends IllegalArgumentException {
    public UnauthorizedAccountBookAccessException() {
        super("속하지 않은 가계부로 인해 조회가 불가합니다.");
    }

    public UnauthorizedAccountBookAccessException(String s) {
        super(s);
    }
}
