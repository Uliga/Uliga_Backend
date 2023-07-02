package com.uliga.uliga_backend.domain.Category.dto;

import com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryAnalyzeQ;
import com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookCategories {
        @Schema(description = "가계부 카테고리들")
        private List<AccountBookCategoryInfoQ> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "카테고리 생성 요청")
    public static class CategoryCreateRequest {
        @NotNull
        @Schema(description = "가계부 아이디")
        private Long id;
        @NotNull
        @Schema(description = "생성할 카테고리들")
        private List<String> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateResult {
        @Schema(description = "가계부 아이디")
        private Long id;
        @Schema(description = "생성한 카테고리값")
        private List<String> created;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석용 한달 지출 조회")
    public static class MonthlyRecordSumPerCategories {
        @Schema(description = "카테고리별 지출들")
        private List<AccountBookCategoryAnalyzeQ> categories;
        private Long sum;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRecordCategory {
        private Long accountBookId;
        private Long recordId;
        private String category;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateIncomeCategory {
        private Long accountBookId;
        private Long incomeId;
        private String category;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCategoryResult {
        private String category;
        private Long updateItemId;
    }
}
