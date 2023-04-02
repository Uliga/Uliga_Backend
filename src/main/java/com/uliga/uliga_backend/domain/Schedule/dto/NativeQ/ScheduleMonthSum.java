package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Schema(description = "한달 금융일정 지출/수입 총합")
public class ScheduleMonthSum {
    private Long value;
    private Boolean isIncome;

    @Builder
    public ScheduleMonthSum(Long value, Boolean isIncome) {
        this.value = value;
        this.isIncome = isIncome;
    }
}