package com.uliga.uliga_backend.domain.Category.exception.handler;

import com.uliga.uliga_backend.domain.Category.exception.DuplicateCategoryException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(DuplicateCategoryException.class)
    protected final ResponseEntity<ErrorResponse> handleDuplicateCategory(
            DuplicateCategoryException ex, WebRequest request
    ) {
        log.info("중복 카테고리");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message(ex.getMessage()).build(),
                HttpStatus.CONFLICT);
    }
}
