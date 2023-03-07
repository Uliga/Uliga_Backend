package com.uliga.uliga_backend.domain.AccountBook.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQuery.AccountBookInfoQ;
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
    public static class AccountBookInfo {
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





}
