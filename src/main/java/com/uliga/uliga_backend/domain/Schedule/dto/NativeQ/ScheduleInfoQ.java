package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleInfoQ {
    private Long id;
    private Long dueDay;
    private Long notificationDay;

    private Long value;
    @Schema(description = "작성자", defaultValue = "creatorNickname")
    private String creator;

    public ScheduleInfoQ(Long id, Long dueDay, Long nDay, Long value, String creator) {
        this.id = id;
        this.dueDay = dueDay;
        this.notificationDay = nDay;
        this.value = value;
        this.creator = creator;
    }
}
