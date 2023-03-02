package com.uliga.uliga_backend.domain.Token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenInfoDTO {
        private String grantType;

        private String accessToken;

        private Long accessTokenExpiresIn;

        private String refreshToken;

        public TokenIssueDTO toTokenIssueDTO() {
            return TokenIssueDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiresIn(accessTokenExpiresIn)
                    .grantType(grantType).build();
        }


    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenIssueDTO {
        private String accessToken;

        private String grantType;

        private Long accessTokenExpiresIn;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class AccessTokenDTO {
        private String accessToken;
    }
}
