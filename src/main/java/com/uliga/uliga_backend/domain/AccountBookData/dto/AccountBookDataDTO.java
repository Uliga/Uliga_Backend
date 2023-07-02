package com.uliga.uliga_backend.domain.AccountBookData.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.DailyValueQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlyCompareQ;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AccountBookDataDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "수입/지출 생성 요청")
    public static class CreateItems {
        @NotNull
        @Schema(description = "가계부 아이디")
        private Long id;
        @NotNull
        @Schema(description = "추가할 아이템들")
        private List<CreateRecordOrIncomeDto> createRequest;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResult {
        @Schema(description = "생성된 지출 수")
        private Long record;

        @Schema(description = "생성된 수입 수")
        private Long income;
        @Schema(description = "생성된 수입/지출 정보")
        private List<CreateItemResult> created;


    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRecordOrIncomeDto {
        @NotNull
        @Schema(description = "수입인지 아닌지")
        private Boolean isIncome;
        @NotNull
        @Schema(description = "카테고리 이름", defaultValue = "category name")
        private String category;
        @NotNull
        @Schema(description = "금융 수단", defaultValue = "payment")
        private String payment;
        @NotNull
        @Schema(description = "날짜", defaultValue = "yyyy-mm-dd")
        private String date;
        @NotNull
        @Schema(description = "거래처", defaultValue = "거래처")
        private String account;
        @NotNull
        private Long value;
        @Schema(description = "간단한 메모", defaultValue = "simple memo")
        private String memo;
        @NotNull
        @Schema(description = "추가할 공유 가계부들")
        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItemResult {
        private Long id;
        @Schema(description = "수입인지 아닌지")
        private Boolean isIncome;
        @Schema(description = "카테고리", defaultValue = "categoryName")
        private String category;
        @Schema(description = "금융 수단", defaultValue = "card/cash/etc")
        private String payment;
        private Long year;
        private Long month;
        private Long day;
        @Schema(description = "거래처", defaultValue = "거래처")
        private String account;
        private Long value;
        @Schema(description = "간단한 메모", defaultValue = "simple memo")
        private String memo;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "한달 대략적 수입/지출조회")
    public static class AccountBookDataDailySum {
        @Schema(description = "날짜별 수입들")
        private List<DailyValueQ> incomes;
        @Schema(description = "날짜별 지출들")
        private List<DailyValueQ> records;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석용 한달 지출 조회")
    public static class AccountBookDailyRecord {
        @Schema(description = "날짜별 지출들")
        private List<DailyValueQ> records;
        private Long sum;
        private Long diff;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeeklySum {
        private Long startDay;
        private Long endDay;
        private Long value;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookWeeklyRecord {
        private List<WeeklySum> weeklySums;
        private Long sum;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordAndIncomeDetails {
        private List<AccountBookDataQ> items;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteItemRequest {
        @NotNull
        private List<Long> deleteIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRecordResult {
        @Schema(description = "가계부 아이디")
        private Long accountBookId;
        @Schema(description = "지출 정보")
        private RecordInfoQ recordInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddIncomeResult {
        @Schema(description = "가계부 아이디")
        private Long accountBookId;
        @Schema(description = "수입 정보")
        private IncomeInfoQ incomeInfo;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "수입 한개 생성 요청")
    public static class AddIncomeRequest {
        @NotNull
        @Schema(description = "가계부 아이디")
        private Long id;
        @NotNull
        @Schema(description = "카테고리", defaultValue = "category")
        private String category;
        @NotNull
        @Schema(description = "결제 수단", defaultValue = "현금/카드/이체 등등")
        private String payment;
        @NotNull
        @Schema(description = "날짜", defaultValue = "yyyy-mm-dd")
        private String date;
        @NotNull
        @Schema(description = "거래처")
        private String account;
        @NotNull
        @Schema(description = "값")
        private Long value;
        @Schema(description = "간단한 메모", defaultValue = "simple memo")
        private String memo;
        @NotNull
        @Schema(description = "추가할 다른 가계부들")
        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "지출 한개 생성 요청")
    public static class AddRecordRequest {
        @NotNull
        @Schema(description = "가계부 아이디")
        private Long id;
        @NotNull
        @Schema(description = "카테고리", defaultValue = "category")
        private String category;
        @NotNull
        @Schema(description = "결제 수단", defaultValue = "현금/카드/이체 등등")
        private String payment;
        @NotNull
        @Schema(description = "날짜", defaultValue = "yyyy-mm-dd")
        private String date;
        @NotNull
        @Schema(description = "거래처")
        private String account;
        @NotNull
        @Schema(description = "값")
        private Long value;
        @Schema(description = "간단한 메모", defaultValue = "simple memo")
        private String memo;
        @NotNull
        @Schema(description = "추가할 다른 가계부들")
        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "가계부 내역 삭제 요청")
    public static class AccountBookDataDeleteRequest {
        @NotNull
        private List<Long> ids;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyCompare {
        private List<MonthlyCompareQ> compare;

    }
}
