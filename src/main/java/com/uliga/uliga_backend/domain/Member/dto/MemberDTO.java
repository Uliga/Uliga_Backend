package com.uliga.uliga_backend.domain.Member.dto;

import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.uliga.uliga_backend.domain.Member.model.Authority.ROLE_USER;

public class MemberDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SignUpRequest {
        @Email
        private String email;

        @Size(min = 8)
        private String password;
        @Size(min = 2, max = 19)
        private String nickName;
        @NotNull
        private String userName;
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
    public static class SignUpResult {
        @Schema(description = "회원가입 결과", defaultValue = "CREATED")
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LoginRequest {
        private String email;
        private String password;


        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LoginResult {
        private MemberInfoNativeQ memberInfo;

        private TokenIssueDTO tokenInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberInfo {
        private Long id;
        private String avatarUrl;
        private String userName;
        private String nickName;
        private String email;
        private String applicationPassword;


    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailConfirmCodeDto {

        private String email;
        private String code;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CodeConfirmDto {
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ConfirmEmailDto {
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class EmailSentDto {
        private String email;
        private boolean success;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ExistsCheckDto {
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
        private String applicationPassword;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdatePasswordDto {
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
        private String password;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class UpdateResult {
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MatchResult {
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NicknameUpdateResult {
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AvatarUrlUpdateResult {
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
        private Long id;

        private String memberName;

        private String accountBookName;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class GetMemberInfo {
        private MemberInfoNativeQ memberInfo;
        private List<InvitationInfo> invitations;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SearchMemberByEmail {
        private String email;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SearchEmailResult {

        private Long id;
        private String userName;

        private String nickName;
    }

}
