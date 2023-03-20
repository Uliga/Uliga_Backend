package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AccountBookMemberInfoQ {

    private Long id;
    @Schema(description = "멤버 닉네임", defaultValue = "memberNickname")
    private String username;
    @Schema(description = "멤버 권한", defaultValue = "memberAuthority")
    private AccountBookAuthority accountBookAuthority;

    public AccountBookMemberInfoQ(Long id, String username, AccountBookAuthority accountBookAuthority) {
        this.id = id;
        this.username = username;
        this.accountBookAuthority = accountBookAuthority;
    }
}
