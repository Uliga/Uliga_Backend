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
    public static class IncomeUpdateRequest {
        private Long id;
        private Long value;
        private String payment;
        private String account;
        private String memo;
        private String category;
        private String date;

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
