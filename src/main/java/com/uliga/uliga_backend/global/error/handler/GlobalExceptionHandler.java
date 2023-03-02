package com.uliga.uliga_backend.global.error.handler;

import com.uliga.uliga_backend.global.error.exception.NotAuthorizedException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundByIdException.class)
    protected final ResponseEntity<ErrorResponse> handleNotFoundByIdException(
            NotFoundByIdException ex, WebRequest request) {
        log.info("해당 아이디로 존재하는 객체를 찾을 수 없습니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(NOT_FOUND)
                .message(ex.getMessage())
                .build()
                , NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected final ResponseEntity<ErrorResponse> handleNotAuthorizedException(
            NotAuthorizedException ex, WebRequest request
    ) {
        log.info("접근 권한이 없는 요청입니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(UNAUTHORIZED)
                .message(ex.getMessage())
                .build()
                , UNAUTHORIZED);
    }
}