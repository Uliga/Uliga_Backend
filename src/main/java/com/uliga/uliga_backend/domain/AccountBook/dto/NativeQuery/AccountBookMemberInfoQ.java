package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class AccountBookMemberInfoQ {

    private Long id;
    private String nickname;
    private AccountBookAuthority accountBookAuthority;

    public AccountBookMemberInfoQ(Long id, String nickname, AccountBookAuthority accountBookAuthority) {
        this.id = id;
        this.nickname = nickname;
        this.accountBookAuthority = accountBookAuthority;
    }
}
