package com.uliga.uliga_backend.domain.Schedule.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
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
        private Long notificationDate;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateScheduleRequest {
        @Schema(description = "아이디")
        private Long id;
        @Schema(description = "금융 일정 이름")
        private String name;
        @Schema(description = "수입/지출 여부")
        private Boolean isIncome;
        @Schema(description = "알림받을 날짜")
        private Long notificationDate;
        @Schema(description = "금액")
        private Long value;
    }
}
