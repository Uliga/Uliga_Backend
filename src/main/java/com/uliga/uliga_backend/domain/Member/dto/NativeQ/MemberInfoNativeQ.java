package com.uliga.uliga_backend.domain.Member.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MemberInfoNativeQ {
    private Long id;
    private Long privateAccountBookId;
    private String avatarUrl;
    private String userName;
    private String nickName;
    private String email;

    public MemberInfoNativeQ(Long id,Long privateAccountBookId, String avatarUrl, String userName, String nickName, String email) {
        this.id = id;
        this.privateAccountBookId = privateAccountBookId;
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
    }
}
