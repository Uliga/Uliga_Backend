package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleInfoQ {
    private Long id;
    private Long notificationDay;
    private String name;
    private Boolean isIncome;
    private Long value;
    @Schema(description = "작성자", defaultValue = "creatorNickname")
    private String creator;
    @Builder
    public ScheduleInfoQ(Long id, Boolean isIncome, String name, Long nDay, Long value, String creator) {
        this.id = id;
        this.isIncome = isIncome;
        this.name = name;
        this.notificationDay = nDay;
        this.value = value;
        this.creator = creator;
    }
}
