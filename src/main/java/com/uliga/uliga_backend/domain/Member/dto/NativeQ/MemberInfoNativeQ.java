package com.uliga.uliga_backend.domain.Member.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MemberInfoNativeQ {
    private Long id;
    private String avatarUrl;
    private String userName;
    private String nickName;
    private String email;

    public MemberInfoNativeQ(Long id, String avatarUrl, String userName, String nickName, String email) {
        this.id = id;
        this.avatarUrl = avatarUrl;
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
    }
}
