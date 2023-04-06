package com.uliga.uliga_backend.domain.Category.exception;

public class DuplicateCategoryException extends IllegalArgumentException {
    public DuplicateCategoryException() {
        super("해당 이름으로 이미 존재하는 카테고리가 있습니다.");
    }

    public DuplicateCategoryException(String s) {
        super(s);
    }
}
