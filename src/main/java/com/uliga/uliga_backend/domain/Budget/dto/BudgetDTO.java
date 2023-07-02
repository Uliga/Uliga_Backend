package com.uliga.uliga_backend.domain.Budget.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetUpdateRequest {
        @Schema(description = "가계부 아이디")
        private Long id;
        @Schema(description = "예산 년도")
        private Long year;
        @Schema(description = "예산 달")
        private Long month;
        @Schema(description = "예산 값")
        private Long value;
        @Schema(description = "예산 카테고리")
        private String category;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAccountBookAssets {

        private MonthlySumQ income;
        private MonthlySumQ record;
        private MonthlySumQ budget;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BudgetCompare {
        private Long budget;
        private Long spend;
        private Long diff;
    }
}
