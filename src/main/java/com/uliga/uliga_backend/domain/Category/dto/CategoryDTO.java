package com.uliga.uliga_backend.domain.Category.dto;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySaveRequest{
        private String name;

        private AccountBook accountBook;

        public Category toEntity() {
            return Category.builder()
                    .name(name)
                    .accountBook(accountBook).build();
        }
    }
}
