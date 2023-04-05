package com.uliga.uliga_backend.domain.Member.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MemberInfoNativeQ {
    private Long id;
    private Long privateAccountBookId;
    @Schema(description = "유저 이름", defaultValue = "사용자 본명")
    private String userName;
    @Schema(description = "유저 닉네임", defaultValue = "사용자 닉네임")
    private String nickName;
    @Schema(description = "유저 이메일", defaultValue = "사용자 이메일")
    private String email;

    public MemberInfoNativeQ(Long id,Long privateAccountBookId, String userName, String nickName, String email) {
        this.id = id;
        this.privateAccountBookId = privateAccountBookId;
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
    }
}
