package com.uliga.uliga_backend.domain.Income.dto;

import com.uliga.uliga_backend.domain.Common.Date;
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
        private Date date;
        private String creator;
        private String category;



    }
}
