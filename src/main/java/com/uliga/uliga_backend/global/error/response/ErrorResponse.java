package com.uliga.uliga_backend.global.error.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    @Schema(description = "에러코드")
    private final Long errorCode;
    @Schema(description = "에러 메시지", defaultValue = "에러 원인 ex) 잘못된 이메일 비번, 존재하지 않는 아이디")
    private final String message;

    @Builder
    public ErrorResponse(Long errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}