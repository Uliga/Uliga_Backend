package com.uliga.uliga_backend.domain.AccountBook.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
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
    public static class CreateRequestPrivate{
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
    public static class GetAccountBookInfos{
        private List<AccountBookInfoQ> accountBooks;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AccountBookInfo{
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
    public static class GetInvitations{
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
    public static class CreateRecordOrIncomeDto{
        private String type;

        private String category;

        private String payment;

        private String account;

        private Long value;

        private String memo;

        private List<Long> sharedAccountBook;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateItems{
        private List<CreateRecordOrIncomeDto> createRequest;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResult {
        private Long record;

        private Long income;
    }





}
