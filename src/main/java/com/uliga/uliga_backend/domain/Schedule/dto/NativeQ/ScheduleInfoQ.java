package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleInfoQ {
    private Long id;
    private Long dueYear;
    private Long dueMonth;
    private Long dueDay;
    private Long notificationYear;
    private Long notificationMonth;
    private Long notificationDay;

    private Long value;

    private String creator;

    public ScheduleInfoQ(Long id, Long dueYear, Long dueMonth, Long dueDay, Long nYear, Long nMonth, Long nDay, Long value, String creator) {
        this.id = id;
        this.dueYear = dueYear;
        this.dueMonth = dueMonth;
        this.dueDay = dueDay;
        this.notificationYear = nYear;
        this.notificationMonth = nMonth;
        this.notificationDay = nDay;
        this.value = value;
        this.creator = creator;
    }
}
