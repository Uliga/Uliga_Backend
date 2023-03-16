package com.uliga.uliga_backend.domain.Budget.exception;

public class BudgetNotExistsException extends IllegalArgumentException {
    public BudgetNotExistsException() {
        super("업데이트하려는 존재하지 않는 예산입니다. 해당 년도, 달에 먼저 예산을 생성해주세요");
    }

    public BudgetNotExistsException(String s) {
        super(s);
    }
}
