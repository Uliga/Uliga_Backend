package com.uliga.uliga_backend.domain.income.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.uliga.uliga_backend.domain.income.exception.InvalidIncomeDeleteRequest;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class IncomeExceptionHandler {
    @ExceptionHandler(InvalidIncomeDeleteRequest.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidIncomeDelete(
            InvalidIncomeDeleteRequest ex, WebRequest request) {
        log.info("올바르지 않은 수입 삭제 요청");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(409L)
                .build(),
                HttpStatus.CONFLICT);
    }
}
