package com.uliga.uliga_backend.domain.Schedule.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ScheduleDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleInfo {
        private Long id;
        private Date dueDate;
        private Date notificationDate;
        private Long value;
        private String creator;

    }
}
