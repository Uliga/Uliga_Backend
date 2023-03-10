package com.uliga.uliga_backend.domain.Record.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecordDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecordInfo {
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordUpdateRequest {
        private Long id;
        private Long value;
        private String payment;
        private String account;
        private String memo;
        private String category;
        private String date;
    }


}
