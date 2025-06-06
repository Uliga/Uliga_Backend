package com.uliga.uliga_backend.domain.account_book.exception;

import com.uliga.uliga_backend.domain.account_book.dto.AccountBookDTO;

public class InvitationSaveErrorWithCreation extends IllegalArgumentException {
    AccountBookDTO.SimpleAccountBookInfo simpleAccountBookInfo;

    public InvitationSaveErrorWithCreation(AccountBookDTO.SimpleAccountBookInfo simpleAccountBookInfo) {

        super();
        this.simpleAccountBookInfo = simpleAccountBookInfo;
    }

    public InvitationSaveErrorWithCreation(String s) {
        super(s);
    }

    public AccountBookDTO.SimpleAccountBookInfo getSimpleAccountBookInfo() {
        return simpleAccountBookInfo;
    }
}
