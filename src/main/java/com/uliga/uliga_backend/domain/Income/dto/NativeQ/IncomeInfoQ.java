package com.uliga.uliga_backend.domain.Income.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class IncomeInfoQ {
    private Long id;
    private Long value;
    private String paymentType;
    private String account;
    private String memo;
    private Date date;
    private String creator;
    private String category;

    public IncomeInfoQ(Long id, Long value, String paymentType, String account, String memo, Date date, String creator, String category) {
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
