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

    public ScheduleInfoQ(Long id, Date dueDate, Date notificationDate, Long value, String creator) {
        this.id = id;
        this.dueDate = dueDate;
        this.notificationDate = notificationDate;
        this.value = value;
        this.creator = creator;
    }
}
