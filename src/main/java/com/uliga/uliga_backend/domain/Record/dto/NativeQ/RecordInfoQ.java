package com.uliga.uliga_backend.domain.Record.dto.NativeQ;

import com.uliga.uliga_backend.domain.AccountBook.model.PaymentType;
import com.uliga.uliga_backend.domain.Common.Date;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RecordInfoQ {
    private Long id;
    private Long value;
    private PaymentType paymentType;
    private String account;
    private String memo;
    private Date date;
    private String creator;
    private String category;

    public RecordInfoQ(Long id, Long value, PaymentType paymentType, String account, String memo, Date date, String creator, String category) {
        this.id = id;
        this.value = value;
        this.paymentType = paymentType;
        this.account = account;
        this.memo = memo;
        this.date = date;
        this.creator = creator;
        this.category = category;
    }
}
