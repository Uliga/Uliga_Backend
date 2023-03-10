package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MonthlySumQ {
    private Long value;

    public MonthlySumQ(Long value) {
        this.value = value;
    }
}
