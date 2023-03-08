package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class AccountBookInfoQ {
    private Long accountBookId;

    private Boolean isPrivate;

    private String accountBookName;

    private AccountBookAuthority accountBookAuthority;

    private Boolean getNotification;

    public AccountBookInfoQ(Long accountBookId, Boolean isPrivate, String accountBookName, AccountBookAuthority accountBookAuthority, Boolean getNotification) {
        this.accountBookId = accountBookId;
        this.isPrivate = isPrivate;
        this.accountBookName = accountBookName;
        this.accountBookAuthority = accountBookAuthority;
        this.getNotification = getNotification;
    }
}
