package com.uliga.uliga_backend.domain.AccountBook.exception.handler;

import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookCategoryCreateException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class AccountBookExceptionHandler {
    @ExceptionHandler(UnauthorizedAccountBookAccessException.class)
    protected final ResponseEntity<ErrorResponse> handleUnauthorizedAccountBook(
            UnauthorizedAccountBookAccessException ex, WebRequest request
    ) {
        log.info("권한이 없는 가계부 접근 요청임");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(401L).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnauthorizedAccountBookCategoryCreateException.class)
    protected final ResponseEntity<ErrorResponse> handleUnauthorizedCategory(
            UnauthorizedAccountBookCategoryCreateException ex, WebRequest request
    ) {
        log.info("권한이 없는 카테고리 생성 요청임");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(401L).build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleCategoryNotFound(
            CategoryNotFoundException ex, WebRequest request
    ) {
        log.info("가계부에 존재하지 않는 카테고리로 지출 혹은 수입 생성 요청이 들어옴");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(404L).build(), HttpStatus.NOT_FOUND);
    }
}
