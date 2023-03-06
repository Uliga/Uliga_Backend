package com.uliga.uliga_backend.global.error.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final Long errorCode;
    private final String message;

    @Builder
    public ErrorResponse(Long errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}