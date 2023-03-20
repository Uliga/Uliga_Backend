package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Schema(description = "카테고리 정보")
public class AccountBookCategoryInfoQ {
    private Long id;
    @Schema(description = "가계부 이름", defaultValue = "categoryName")
    private String name;

    @Schema(description = "가계부 레이블", defaultValue = "categoryLabel")
    private String label;
    @Builder
    public AccountBookCategoryInfoQ(Long id, String name) {
        this.id = id;
        this.name = name;
        this.label = name;
    }

    public AccountBookCategoryInfoQ(String label) {
        this.label = label;
    }
}
