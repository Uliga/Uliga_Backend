package com.uliga.uliga_backend.domain.Schedule.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateScheduleRequest {
        @NotNull
        private String name;
        @NotNull
        private Boolean isIncome;
        @NotNull
        private Long notificationDate;
        private Long value;
    }
}
