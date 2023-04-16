package com.uliga.uliga_backend.global.oauth2.exception.handler;

import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.oauth2.exception.DuplicateUserByEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class OAuthExceptionHandler {
    @ExceptionHandler(DuplicateUserByEmail.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicateUserByEmail(
            DuplicateUserByEmail ex, WebRequest request
    ) {
        log.info("이메일로 존재하는 멤버");
        return new ResponseEntity<>(ErrorResponse.builder().errorCode(503L).message(ex.getMessage()).build()
                , HttpStatus.SERVICE_UNAVAILABLE);
    }
}
