package com.uliga.uliga_backend.domain.Income.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IncomeDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IncomeInfo {
        private Long id;
        private Long value;
        private String payment;
        private String account;
        private String memo;
        @Embedded
        private Date date;
        private String creator;
        private String category;

    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "수입 업데이트 요청")
    public static class IncomeUpdateRequest {
        @Schema(description = "수입 아이디")
        private Long id;
        @Schema(description = "값")
        private Long value;
        @Schema(description = "결제 수단", defaultValue = "카드/현금/이체 등등")
        private String payment;
        @Schema(description = "거래처", defaultValue = "거래처")
        private String account;
        @Schema(description = "간단한 메모", defaultValue = "simple memo")
        private String memo;
        @Schema(description = "카테고리", defaultValue = "newCategory")
        private String category;
        @Schema(description = "날짜", defaultValue = "yyyy-mm-dd")
        private String date;
        @Schema(description = "타입", defaultValue = "RECORD")
        private String type;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "수입 삭제 요청")
    public static class IncomeDeleteRequest {
        @Schema(description = "수입 아이디")
        private Long id;
    }


}
