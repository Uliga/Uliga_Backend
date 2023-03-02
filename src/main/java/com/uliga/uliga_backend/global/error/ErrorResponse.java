package com.uliga.uliga_backend.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final String message;

    @Builder
    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}