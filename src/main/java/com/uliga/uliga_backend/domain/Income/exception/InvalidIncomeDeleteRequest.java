package com.uliga.uliga_backend.domain.Income.exception;

public class InvalidIncomeDeleteRequest extends IllegalArgumentException {
    public InvalidIncomeDeleteRequest() {
        super("수입 생성자가 아니라서 삭제할 수 없습니다");
    }

    public InvalidIncomeDeleteRequest(String s) {
        super(s);
    }
}
