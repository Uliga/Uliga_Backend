package com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ;

import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Schema(description = "가계부 내역 조회")
public class AccountBookDataQ {
    @Schema(description = "수입 혹은 지출 아이디")
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
    @Schema(description = "수입/지출 여부")
    private AccountBookDataType type;
    @Schema(description = "작성자", defaultValue = "creatorNickname")
    private String creator;
    @Schema(description = "카테고리", defaultValue = "category")
    private String category;
    @Schema(description = "아바타 색")
    private String avatarUrl;
    @Builder
    public AccountBookDataQ(Long id, Long value, String payment, String account, String memo, Long year, Long month, Long day, AccountBookDataType type, String creator, String category, String avatarUrl) {
        this.id = id;
        this.value = value;
        this.payment = payment;
        this.account = account;
        this.memo = memo;
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
        this.creator = creator;
        this.category = category;
        this.avatarUrl = avatarUrl;
    }
}
