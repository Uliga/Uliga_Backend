package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class WeeklySumQ {
    @Schema(description = "일주일 동안의 합")
    private Long value;

    public WeeklySumQ(Long value) {
        this.value = value;
    }
}
