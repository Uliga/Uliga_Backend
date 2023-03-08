package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleInfoQ {
    private Long id;
    @Embedded
    private Date dueDate;
    @Embedded
    private Date notificationDate;
    private Long value;

    private String creator;

    public ScheduleInfoQ(Long id, Long dueYear, Long dueMonth, Long dueDay, Long nYear, Long nMonth, Long nDay, Long value, String creator) {
        this.id = id;
        this.dueDate = Date.builder()
                .day(dueDay)
                .month(dueMonth)
                .year(dueYear).build();
        this.notificationDate = Date.builder()
                .day(nDay)
                .month(nMonth)
                .year(nYear).build();
        this.value = value;
        this.creator = creator;
    }
}
