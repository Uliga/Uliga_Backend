package com.uliga.uliga_backend.domain.Budget.exception.handler;

import com.uliga.uliga_backend.domain.Budget.exception.BudgetNotExistsException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class BudgetExceptionHandler {

    @ExceptionHandler(BudgetNotExistsException.class)
    protected final ResponseEntity<ErrorResponse> handleBudgetNotExists(
            BudgetNotExistsException ex, WebRequest request
    ) {
        log.info("업데이트 하려는 예산이 존재하지 않음");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(409L).build(),
                HttpStatus.CONFLICT);
    }
}
