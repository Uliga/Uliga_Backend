package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class AccountBookInfoQ {
    @Schema(description = "가계부 아이디")
    private Long accountBookId;
    @Schema(description = "공유가계부 인지 아닌지")
    private Boolean isPrivate;

    @Schema(description = "가계부 이름", defaultValue = "accountBookName")
    private String accountBookName;
    @Schema(description = "가계부에 대한 권한")
    private AccountBookAuthority accountBookAuthority;
    @Schema(description = "가계부 별칭", defaultValue = "relationship")
    private String relationShip;

    private Boolean getNotification;
    @Schema(description = "아바타 색상", defaultValue = "avatarUrl")
    private String avatarUrl;

    public AccountBookInfoQ(Long accountBookId, Boolean isPrivate, String accountBookName, AccountBookAuthority accountBookAuthority, Boolean getNotification, String relationShip, String avatarUrl) {
        this.accountBookId = accountBookId;
        this.isPrivate = isPrivate;
        this.accountBookName = accountBookName;
        this.accountBookAuthority = accountBookAuthority;
        this.getNotification = getNotification;
        this.relationShip = relationShip;
        this.avatarUrl = avatarUrl;
    }
}
