package com.uliga.uliga_backend.domain.Budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BudgetDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBudgetDto {
        @Schema(description = "가계부 아이디")
        private Long id;
        @Schema(description = "예산년도")
        private Long year;
        @Schema(description = "예산 달")
        private Long month;
        @Schema(description = "예산 값")
        private Long value;
        @Schema(description = "예산 카테고리")
        private String category;

    }

}
