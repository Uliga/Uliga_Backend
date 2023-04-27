package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Schema(description = "카테고리 별 지출 정보")
public class AccountBookCategoryAnalyzeQ {
    private Long id;

    private String name;

    private Long value;
    @Builder
    public AccountBookCategoryAnalyzeQ(Long id, String name, Long value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
