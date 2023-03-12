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
        @Schema(description = "회원가입 이메일", defaultValue = "test@email.com")
        @Email
        private String email;
        @Schema(description = "회원가입 비밀번호", defaultValue = "12345678")
        @Size(min = 8)
        private String password;
        @Schema(description = "닉네임", defaultValue = "nickName")
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
    public static class SignUpResult {
        @Schema(description = "회원가입 결과", defaultValue = "CREATED")
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class LoginRequest {
        @Schema(description = "로그인할 이메일", defaultValue = "test@email.com")
        private String email;
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
        @Schema(description = "일치시", example = "true", defaultValue = "false")
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ConfirmEmailDto {
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
        @Schema(name = "존재여부", description = "존재여부", defaultValue = "true")
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
        @Schema(name = "업데이트 결과", defaultValue = "업데이트한 결과")
        private String result;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class MatchResult {
        @Schema(name = "일치 여부", defaultValue = "true")
        private boolean matches;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class NicknameUpdateResult {
        @Schema(description = "업데이트한 닉네임", name = "업데이트할 닉네임", defaultValue = "newNickname")
        private String nickname;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AvatarUrlUpdateResult {
        @Schema(description = "업데이트한 프사", name = "업데이트할 프사", defaultValue = "newProfile")
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
        @Schema(name = "가계부 아이디", description = "초대온 가계부 아이디", defaultValue = "1")
        private Long id;
        @Schema(name = "초대한 멤버 이름", description = "자신을 초대한 사람", defaultValue = "testUser")
        private String memberName;
        @Schema(name = "초대 받은 가계부 이름", defaultValue = "초대 받은 가계부", description = "testUser님의 가계부")
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

        @Schema(name = "멤버 아이디", description = "이메일로 찾은 멤버 아이디")
        private Long id;
        @Schema(name = "찾은 멤버 유저네임", description = "이메일로 찾은 멤버 이름", defaultValue = "userName")
        private String userName;
        @Schema(name = "찾은 멤버 닉네임", defaultValue = "이메일로 찾은 멤버 닉네임", description = "nickName")
        private String nickName;
    }

}
