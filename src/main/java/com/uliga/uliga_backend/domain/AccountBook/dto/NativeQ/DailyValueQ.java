package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Schema(description = "하루 수입/지출")
public class DailyValueQ {

    @Schema(description = "날짜")
    private Long day;
    @Schema(description = "값")
    private Long value;

    @Builder
    public DailyValueQ(Long day, Long value) {

        this.day = day;
        this.value = value;
    }
}
