package com.uliga.uliga_backend.domain.Budget.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class BudgetInfoQ {
    @Schema(description = "예산 아이디")
    private Long id;
    @Schema(description = "해당 예산 년도")
    private Long year;
    @Schema(description = "해당 예산 달")
    private Long month;
    @Schema(description = "해당 예산 값")
    private Long value;
    @Schema(description = "해당 예산 카테고리")
    private String categoryName;
    @Builder
    public BudgetInfoQ(Long id, Long year, Long month, Long value, String categoryName) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.value = value;
        this.categoryName = categoryName;
    }
}
