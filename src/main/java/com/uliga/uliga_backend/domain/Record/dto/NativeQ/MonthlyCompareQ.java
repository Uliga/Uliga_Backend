package com.uliga.uliga_backend.domain.Record.dto.NativeQ;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MonthlyCompareQ {
    private Long year;
    private Long month;
    private Long value;
    @Builder
    public MonthlyCompareQ(Long year, Long month, Long value) {
        this.year = year;
        this.month = month;
        this.value = value;
    }
}
