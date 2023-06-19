package com.uliga.uliga_backend.domain.AccountBook.exception;

import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.SimpleAccountBookInfo;

public class InvitationSaveError extends IllegalArgumentException {



    public InvitationSaveError() {
        super("레디스 저장과정에서 오류가 발생하였습니다");
    }


    public InvitationSaveError(String s) {
        super(s);
    }
}
