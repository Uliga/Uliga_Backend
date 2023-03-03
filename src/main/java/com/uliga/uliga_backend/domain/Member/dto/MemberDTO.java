package com.uliga.uliga_backend.domain.Member.dto;

import com.uliga.uliga_backend.domain.Member.dto.NativeQuery.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.uliga.uliga_backend.domain.Member.model.Authority.ROLE_USER;
import static com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;

public class MemberDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SignUpRequest {
        private String email;

        private String password;

        private String nickName;

        private String userName;

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
    public static class SignUpResult {
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

}
