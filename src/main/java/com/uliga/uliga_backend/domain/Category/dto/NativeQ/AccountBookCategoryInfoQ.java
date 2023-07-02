package com.uliga.uliga_backend.domain.Category.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Schema(description = "카테고리 정보")
public class AccountBookCategoryInfoQ {
    private Long id;
    @Schema(description = "가계부 이름", defaultValue = "categoryNameValue")
    private String value;

    @Schema(description = "가계부 레이블", defaultValue = "categoryLabel")
    private String label;
    @Builder
    public AccountBookCategoryInfoQ(Long id, String name) {
        this.id = id;
        this.value = name;
        this.label = name;
    }

    public AccountBookCategoryInfoQ(String label) {
        this.label = label;
    }
}
