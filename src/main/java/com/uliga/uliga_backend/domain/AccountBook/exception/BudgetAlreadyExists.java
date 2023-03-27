package com.uliga.uliga_backend.domain.AccountBook.exception;

public class BudgetAlreadyExists extends IllegalArgumentException {
    public BudgetAlreadyExists() {
        super("해당 년도와 달에 이미 예산이 존재합니다. 예산 업데이트를 통해서 처리해주세요.");
    }

    public BudgetAlreadyExists(String s) {
        super(s);
    }
}
