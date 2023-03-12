package com.uliga.uliga_backend.domain.Income.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class IncomeInfoQ {
    private Long id;
    @Schema(description = "금액")
    private Long value;
    @Schema(description = "지불/입금 수단", defaultValue = "카드/이체/등등")
    private String payment;
    @Schema(description = "거래처", defaultValue = "거래처")
    private String account;
    @Schema(description = "간단한 메모", defaultValue = "simple memo")
    private String memo;
    private Long year;
    private Long month;
    private Long day;
    @Schema(description = "작성자", defaultValue = "creatorNickname")
    private String creator;
    @Schema(description = "카테고리", defaultValue = "category")
    private String category;
    @Builder
    public IncomeInfoQ(Long id, Long value, String payment, String account, String memo, Long year, Long month, Long day, String creator, String category) {
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
