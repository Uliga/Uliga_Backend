package com.uliga.uliga_backend.domain.AccountBook.model;

public enum PaymentType {
    CASH("현금"),
    CARD("카드"),
    ACCOUNT_TRANSFER("이체");

    PaymentType(String value){
    }
}
