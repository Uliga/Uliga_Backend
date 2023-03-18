package com.uliga.uliga_backend.domain.Member.dto;

import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static com.uliga.uliga_backend.domain.Member.model.Authority.ROLE_USER;

public class MemberDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "회원가입 요청")
    public static class SignUpRequest {
        @Schema(description = "회원가입 이메일", defaultValue = "test@email.com")
        @Email
        private String email;
        @Schema(description = "회원가입 비밀번호", defaultValue = "12345678")
        @NotNull
        @Size(min = 8)
        private String password;
        @Schema(description = "닉네임", defaultValue = "nickName")
        @NotNull
        @Size(min = 2, max = 19)
        private String nickName;
        @Schema(description = "본명", defaultValue = "userName")
        @NotNull
        private String userName;
        @Schema(description = "애플리케이션 비밀번호", defaultValue = "1234")
        @NotNull
        private String applicationPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(this.password);
            this.applicationPassword = passwordEncoder.encode(this.applicationPassword);
        }

        public Member toEntity() {
            return Member.builder()
                    .applicationPassword(applicationPassword)
                    .authority(ROLE_USER)
                    .email(email)
                    .userLoginType(UserLoginType.EMAIL)
                    .avatarUrl("default")
                    .nickName(nickName)
                    .userName(userName)
                    .password(password).build();
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SocialSignUpRequest {
        private String email;
        private UserLoginType userLoginType;

        private String userName;

        private String password;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(this.password);
        }

        public Member toEntity() {
            return Member.builder()
                    .userName(userName)
                    .userLoginType(userLoginType)
                    .authority(ROLE_USER)
                    .email(email)
                    .password(password).build();
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(name = "회원가입 결과")
    public static class SignUpResult {
        @Schema(description = "회원가입 결과", defaultValue = "CREATED")
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(name = "로그인 요청")
    public static class LoginRequest {
        @NotNull
        @Email
        @Schema(description = "로그인할 이메일", defaultValue = "test@email.com")
        private String email;
        @NotNull
        @Size(min = 8)
        @Schema(description = "비밀번호", defaultValue = "12345678")
        private String password;


        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(name = "로그인 후 리턴 데이터")
    public static class LoginResult {
        @Schema(name = "멤버 정보")
        private MemberInfoNativeQ memberInfo;
        @Schema(name = "토큰 정보")
        private TokenIssueDTO tokenInfo;
    }



    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "멤버 정보 업데이트 요청시 들어갈 수 있는 값")
    public static class MemberInfoUpdateRequest {
        @Schema(description = "변경할 닉네임")
        private String nickName;
        @Schema(description = "변경할 프로필")
        private String avatarUrl;
        @Schema(description = "변경할 애플리케이션 비밀번호")
        private String applicationPassword;
        @Schema(description = "변경할 비밀번호")
        private String password;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailConfirmCodeDto {
        @NotNull
        @Email
        private String email;
        @NotNull
        @Size(min = 6)
        private String code;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CodeConfirmDto {
        @Schema(description = "일치시", example = "true", defaultValue = "false")
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ConfirmEmailDto {
        @NotNull
        @Email
        @Schema(description = "인증 요청 보낼 이메일", defaultValue = "test@email.com")
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailSentDto {
        @Schema(description = "이메일 인증 요청 보낸 이메일", defaultValue = "test@email.com")
        private String email;
        @Schema(description = "성공 여부", defaultValue = "true")
        private boolean success;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ExistsCheckDto {
        @Schema(description = "존재여부", defaultValue = "true")
        private boolean exists;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateApplicationPasswordDto {
        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.newPassword = passwordEncoder.encode(this.newPassword);
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ApplicationPasswordCheck {
        @NotNull
        @Size(min = 4)
        private String applicationPassword;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdatePasswordDto {
        @Size(min = 8)
        private String newPassword;

        public void encrypt(PasswordEncoder passwordEncoder) {
            this.newPassword = passwordEncoder.encode(this.newPassword);
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PasswordCheck {
        @Size(min = 8)
        private String password;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateResult {
        @Schema(description = "업데이트 결과", defaultValue = "업데이트한 결과")
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MatchResult {
        @Schema(description = "일치 여부", defaultValue = "true")
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NicknameUpdateResult {
        @Schema(description = "업데이트한 닉네임", defaultValue = "newNickname")
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AvatarUrlUpdateResult {
        @Schema(description = "업데이트한 프사", defaultValue = "newProfile")
        private String avatarUrl;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateAvatarUrl {
        private String avatarUrl;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NicknameCheckDto {
        @NotNull
        @Size(min = 2)
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateNicknameDto {
        private String newNickname;
    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class InvitationInfo {
        @Schema(description = "초대온 가계부 아이디", defaultValue = "1")
        private Long id;
        @Schema(description = "자신을 초대한 사람", defaultValue = "testUser")
        private String memberName;
        @Schema( defaultValue = "초대 받은 가계부", description = "testUser님의 가계부")
        private String accountBookName;
        @Schema(description = "생성 시간, 삭제할때도 보내줘야행")
        private LocalDateTime createdTime;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Schema(description = "로그인한 멤버 정보 조회")
    public static class GetMemberInfo {
        @Schema(description = "멤버 정보")
        private MemberInfoNativeQ memberInfo;
        @Schema(description = "초대 정보")
        private List<InvitationInfo> invitations;
        @Schema(description = "금융 일정 알림")

        private List<NotificationInfo> notifications;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class NotificationInfo {
        @Schema(description = "금융일정 이름")
        private String scheduleName;
        @Schema(description = "금융 일정 생성자 이름")
        private String creatorName;
        @Schema(description = "할당 받은 값")
        private Long value;
        @Schema(description = "금융 일정 날짜")
        private Long day;
    }



    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SearchMemberByEmail {
        @NotNull
        @Email
        private String email;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SearchEmailResult {

        @Schema( description = "이메일로 찾은 멤버 아이디")
        private Long id;
        @Schema(description = "이메일로 찾은 멤버 이름", defaultValue = "userName")
        private String userName;
        @Schema(defaultValue = "nickName", description = "이메일로 찾은 멤버 닉네임")
        private String nickName;
    }

}
