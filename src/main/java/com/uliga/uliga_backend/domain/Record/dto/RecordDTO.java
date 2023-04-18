package com.uliga.uliga_backend.domain.Record.dto;

import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "지출 업데이트 요청")
    public static class RecordUpdateRequest {
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
        @Schema(description = "데이터 타입", defaultValue = "INCOME")
        private String type;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordInfoDetail {
        private RecordInfoQ recordInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "지출 삭제 요청")
    public static class RecordDeleteRequest {
        @Schema(description = "지출 아이디")
        private Long id;
    }


}
