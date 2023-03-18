package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleMemberInfoQ {
    private Long id;
    private String nickname;
    private Long value;

    public ScheduleMemberInfoQ(Long id,String nickname, Long value) {
        this.id = id;
        this.nickname = nickname;
        this.value = value;
    }
}
