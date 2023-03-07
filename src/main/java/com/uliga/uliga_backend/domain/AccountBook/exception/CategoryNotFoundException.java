package com.uliga.uliga_backend.domain.AccountBook.exception;

public class CategoryNotFoundException extends IllegalArgumentException {
    public CategoryNotFoundException() {
        super("가계부에 존재하지 않는 카테고리입니다.");

    }

    public CategoryNotFoundException(String s) {
        super(s);
    }
}
