package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AccountBookCategoryInfoQ {
    private Long id;
    @Schema(description = "가계부 이름", defaultValue = "categoryName")
    private String name;

    public AccountBookCategoryInfoQ(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
