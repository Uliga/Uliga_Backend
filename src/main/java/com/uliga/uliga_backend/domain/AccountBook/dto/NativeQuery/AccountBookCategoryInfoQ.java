package com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AccountBookCategoryInfoQ {
    private Long id;
    private String name;

    public AccountBookCategoryInfoQ(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
