package com.uliga.uliga_backend.domain.Record.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RecordInfoQ {
    private Long id;
    private Long value;
    private String payment;
    private String account;
    private String memo;
    private Long year;
    private Long month;
    private Long day;
    private String creator;
    private String category;

    public RecordInfoQ(Long id, Long value, String payment, String account, String memo, Long year, Long month, Long day, String creator, String category) {
        this.id = id;
        this.value = value;
        this.payment = payment;
        this.account = account;
        this.memo = memo;
        this.year = year;
        this.month = month;
        this.day = day;

        this.creator = creator;
        this.category = category;
    }
}
