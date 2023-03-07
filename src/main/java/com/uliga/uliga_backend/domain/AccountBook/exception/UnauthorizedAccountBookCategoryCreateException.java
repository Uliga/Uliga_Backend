package com.uliga.uliga_backend.domain.AccountBook.exception;

public class UnauthorizedAccountBookCategoryCreateException extends IllegalArgumentException {
    public UnauthorizedAccountBookCategoryCreateException() {
        super("속하지 않은 가계부에 카테고리를 추가할 수 없습니다.");
    }

    public UnauthorizedAccountBookCategoryCreateException(String s) {
        super(s);
    }
}
