package com.uliga.uliga_backend.domain.Schedule.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
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
        @Embedded
        private Date dueDate;
        @Embedded
        private Date notificationDate;
        private Long value;
        private String creator;

    }
}
