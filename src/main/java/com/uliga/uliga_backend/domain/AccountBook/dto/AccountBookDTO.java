package com.uliga.uliga_backend.domain.AccountBook.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
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
        private String name;

        private List<String> categories;

        private List<String> emails;

        public AccountBook toEntity() {
            return AccountBook.builder()
                    .isPrivate(false)
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

        public AccountBook toEntity() {
            return AccountBook.builder()
                    .isPrivate(isPrivate)
                    .name(name).build();
        }
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GetAccountBookInfos {
        private List<AccountBookInfoQ> accountBooks;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AccountBookInfo {
        private AccountBookInfoQ info;
        private List<AccountBookMemberInfoQ> members;
        private List<AccountBookCategoryInfoQ> categories;
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SimpleAccountBookInfo {
        private Long id;

        private String name;

        private Boolean isPrivate;


    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GetInvitations {
        private Long id;
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
        private Long id;
        private Boolean join;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InvitationReplyResult {
        private Long id;
        private Boolean join;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRecordOrIncomeDto {
        private Boolean isIncome;

        private String category;

        private String payment;

        private String date;

        private String account;

        private Long value;

        private String memo;

        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItemResult {
        private Long id;

        private Boolean isIncome;

        private String category;

        private String payment;

        private Long year;
        private Long month;
        private Long day;


        private String account;

        private Long value;

        private String memo;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItems {
        private Long id;
        private List<CreateRecordOrIncomeDto> createRequest;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResult {
        private Long record;

        private Long income;

        private List<CreateItemResult> created;


    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookCategories {
        private List<AccountBookCategoryInfoQ> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookMembers {
        private List<AccountBookMemberInfoQ> members;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateRequest {
        private Long id;
        private List<String> categories;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateResult {
        private Long id;
        private List<String> created;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookItems {
        private List<IncomeInfoQ> incomes;
        private List<RecordInfoQ> records;
        private List<ScheduleInfoQ> schedules;
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
        private Long accountBookId;
        private RecordInfoQ recordInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddIncomeResult {
        private Long accountBookId;
        private IncomeInfoQ incomeInfo;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddIncomeRequest {
        private Long id;
        private String category;

        private String payment;

        private String date;

        private String account;

        private Long value;

        private String memo;

        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRecordRequest {
        private Long id;
        private String category;

        private String payment;

        private String date;

        private String account;

        private Long value;

        private String memo;

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


}
