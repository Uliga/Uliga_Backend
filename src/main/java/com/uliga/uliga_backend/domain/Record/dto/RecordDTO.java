package com.uliga.uliga_backend.domain.Record.dto;

import com.uliga.uliga_backend.domain.AccountBook.model.PaymentType;
import com.uliga.uliga_backend.domain.Common.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecordDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecordInfo {
        private Long id;
        private Long value;
        private PaymentType paymentType;
        private String account;
        private String memo;
        private Date date;
        private String creator;
        private String category;
    }


}
