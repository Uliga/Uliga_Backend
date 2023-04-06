package com.uliga.uliga_backend.domain.Category.dto;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CategoryDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "카테고리 생성 요청")
    public static class CategorySaveRequest{
        @Schema(description = "생성할 카테고리 이름")
        private String name;

        private AccountBook accountBook;

        public Category toEntity() {
            return Category.builder()
                    .name(name)
                    .accountBook(accountBook).build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "카테고리 업데이트 요청")
    public static class CategoryUpdateRequest {
        @Schema(description = "변경할 카테고리 이름")
        private String name;
    }
}
