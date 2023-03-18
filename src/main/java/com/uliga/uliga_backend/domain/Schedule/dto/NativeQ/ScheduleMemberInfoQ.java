package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleMemberInfoQ {
    private String nickname;
    private Long value;

    public ScheduleMemberInfoQ(String nickname, Long value) {
        this.nickname = nickname;
        this.value = value;
    }
}
