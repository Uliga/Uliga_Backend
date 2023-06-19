package com.uliga.uliga_backend.domain.Token.exception.handler;

import com.uliga.uliga_backend.domain.Token.exception.ExpireRefreshTokenException;
import com.uliga.uliga_backend.domain.Token.exception.InvalidRefreshTokenException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class TokenExceptionHandler {
    @ExceptionHandler(ExpireRefreshTokenException.class)
    protected final ResponseEntity<ErrorResponse> handleExpireRefreshTokenException(
            ExpireRefreshTokenException ex, WebRequest request
    ) {
        log.warn("만료된 리프레쉬 토큰입니다. 재로그인 해야함");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(401L)
                .message("만료된 리프레쉬 토큰입니다. 재로그인 해주세요.")
                .build()
                , HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(
            InvalidRefreshTokenException ex, WebRequest request
    ) {
        log.warn("유효하지 않은 리프레쉬 토큰입니다.");
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message("유효하지 않은 리프레쉬 토큰입니다.")
                        .errorCode(401L)
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }


}
