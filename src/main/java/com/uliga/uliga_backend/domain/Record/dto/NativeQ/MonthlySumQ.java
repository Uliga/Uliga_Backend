package com.uliga.uliga_backend.domain.Record.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MonthlySumQ {
    @Schema(description = "한달 동안의 합")
    private Long value;

    public MonthlySumQ(Long value) {
        this.value = value;
    }
}
