package com.uliga.uliga_backend.domain.AccountBook.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.CreateScheduleRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AccountBookDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateRequest {
        @NotNull
        @Schema(description = "가계부 이름", defaultValue = "acountBookName")
        private String name;
        @NotNull
        @Schema(description = "생성할 카테고리들")
        private List<String> categories;
        @NotNull
        @Schema(description = "초대할 이메일들")
        private List<String> emails;
        @Schema(description = "가계부 별칭", defaultValue = "relationship")
        private String relationship;

        public AccountBook toEntity() {
            return AccountBook.builder()
                    .isPrivate(false)
                    .relationShip(relationship)
                    .name(name).build();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CreateRequestPrivate {
        private String name;

        private Boolean isPrivate;

        private String relationship;

        public AccountBook toEntity() {
            return AccountBook.builder()
                    .isPrivate(isPrivate)
                    .relationShip(relationship)
                    .name(name).build();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GetAccountBookInfos {
        @Schema(description = "가계부 정보 리스트")
        private List<AccountBookInfoQ> accountBooks;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AccountBookInfo {
        @Schema(description = "가계부 정보")
        private AccountBookInfoQ info;
        @Schema(description = "가계부 멤버 수")
        private MembersQ numberOfMember;
        @Schema(description = "가계부 멤버 정보")
        private List<AccountBookMemberInfoQ> members;
        @Schema(description = "가계부 카테고리 정보")
        private List<AccountBookCategoryInfoQ> categories;
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SimpleAccountBookInfo {
        private Long id;
        @Schema(description = "가계부 이름", defaultValue = "accountBookName")
        private String name;
        @Schema(description = "공개인지 아닌지")
        private Boolean isPrivate;
        @Schema(description = "가계부 별칭", defaultValue = "relationship")
        private String relationShip;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GetInvitations {
        @NotNull
        private Long id;
        @NotNull
        @Schema(description = "초대한 사람 이메일", defaultValue = "invited@email.com")
        private List<String> emails;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Invited {
        private Long invited;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InvitationReply {
        @NotNull
        private Long id;
        @NotNull
        private String memberName;
        @NotNull
        private String accountBookName;
        @NotNull
        private Boolean join;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InvitationReplyResult {
        private Long id;
        @Schema(description = "가계부에 들어왔는지 아닌지")
        private Boolean join;
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
    public static class AccountBookCategories {
        @Schema(description = "가계부 카테고리들")
        private List<AccountBookCategoryInfoQ> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookMembers {
        @Schema(description = "가계부 멤버들")
        private List<AccountBookMemberInfoQ> members;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
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
    @Schema(description = "한달 대략적 수입/지출조회")
    public static class AccountBookIncomesAndRecords {
        @Schema(description = "날짜별 수입들")
        private List<DailyValueQ> incomes;
        @Schema(description = "날짜별 지출들")
        private List<DailyValueQ> records;
    }

//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Schema(description = "한달 금융 일정 조회")
//    public static class AccountBookSchedules {
//        @Schema(description = "금융 일정들")
//        private List<ScheduleInfoQ> schedules;
//    }





    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecordAndIncomeDetails {
        @Schema(description = "수입들")
        private List<IncomeInfoQ> incomes;
        @Schema(description = "지출들")
        private List<RecordInfoQ> records;
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
    public static class GetAccountBookAssets {

        private MonthlySumQ income;
        private MonthlySumQ record;
        private MonthlySumQ budget;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSchedules {
        private Long id;
        private List<CreateScheduleRequest> schedules;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddScheduleResult {
        private List<CreateScheduleRequest> result;
    }


}
