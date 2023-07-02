package com.uliga.uliga_backend.domain.AccountBook.dto;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class AccountBookDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AccountBookCreateRequest {
        @NotNull
        @Schema(description = "가계부 이름", defaultValue = "accountBookName")
        private String name;
        @NotNull
        @Schema(description = "생성할 카테고리들")
        private List<String> categories;
        @NotNull
        @Schema(description = "초대할 이메일들")
        private List<String> emails;
        @NotNull
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
        private List<AccountBookInfo> accountBooks;
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
        @Schema(description = "가계부 아이디")
        @NotNull
        private Long id;
        @Schema(description = "초대자 이름")
        @NotNull
        private String memberName;
        @Schema(description = "가계부 이름")
        @NotNull
        private String accountBookName;
        @Schema(description = "조인 여부")
        @NotNull
        private Boolean join;

        @Override
        public String toString() {
            return "InvitationReply = id: " + this.id + " memberName: " + this.memberName + " accountBookName: " + this.accountBookName + " join: " + this.join;
        }
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
    public static class AccountBookMembers {
        @Schema(description = "가계부 멤버들")
        private List<AccountBookMemberInfoQ> members;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "가계부 생성 요청")
    public static class AccountBookDeleteRequest {
        @Schema(description = "삭제할 가계부 아이디")
        @NotNull
        private Long accountBookId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "가계부 정보 수정 요청")
    public static class AccountBookUpdateRequest {
        @Schema(description = "변경할 이름")
        private String name;
        @Schema(description = "변경할 별칭")
        private String relationship;
        @Schema(description = "변경할 카테고리")
        private List<String> categories;
        @Schema(description = "초대할 멤버")
        private List<String> members;
        @Schema(description = "변경할 아바타")
        private String avatarUrl;
    }

}
