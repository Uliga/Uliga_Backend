package com.uliga.uliga_backend.domain.AccountBook.exception;

public class InvalidAccountBookDeleteRequest extends IllegalArgumentException {
    public InvalidAccountBookDeleteRequest() {
        super("해당 가계부에 속하지 않아서 삭제할 수 없습니다");
    }

    public InvalidAccountBookDeleteRequest(String s) {
        super(s);
    }
}
