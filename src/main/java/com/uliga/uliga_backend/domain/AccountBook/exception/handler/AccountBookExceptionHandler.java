package com.uliga.uliga_backend.domain.AccountBook.exception.handler;

import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.SimpleAccountBookInfo;
import com.uliga.uliga_backend.domain.AccountBook.exception.*;
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
        log.warn("권한이 없는 가계부 접근 요청임");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(400L).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccountBookCategoryCreateException.class)
    protected final ResponseEntity<ErrorResponse> handleUnauthorizedCategory(
            UnauthorizedAccountBookCategoryCreateException ex, WebRequest request
    ) {
        log.warn("권한이 없는 카테고리 생성 요청임");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(400L).build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleCategoryNotFound(
            CategoryNotFoundException ex, WebRequest request
    ) {
        log.warn("가계부에 존재하지 않는 카테고리로 지출 혹은 수입 생성 요청이 들어옴");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(404L).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAccountBookDeleteRequest.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidAccountBookDeleteRequest(
            InvalidAccountBookDeleteRequest ex, WebRequest request
    ) {
        log.warn("해당 가계부에 속하지 않아서 삭제할 수 없습니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message(ex.getMessage()).build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvitationSaveError.class)
    protected final ResponseEntity<ErrorResponse> handleInvitationSaveError(
            InvitationSaveError ex, WebRequest request
    ) {
        log.warn("레디스 저장중 오류 발생");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message("멤버 초대 과정에서 오류가 발생하였습니다.").build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvitationSaveErrorWithCreation.class)
    protected final ResponseEntity<SimpleAccountBookInfo> handleInvitationSaveErrorWithCreation(
            InvitationSaveErrorWithCreation ex, WebRequest request
    ) {
        log.warn("가계부는 생성되었지만, 과정에서 오류 발생");
        return new ResponseEntity<>(ex.getSimpleAccountBookInfo(), HttpStatus.ACCEPTED);
    }

    @ExceptionHandler(BudgetAlreadyExists.class)
    protected final ResponseEntity<ErrorResponse> handleBudgetAlreadyExists(
            BudgetAlreadyExists ex, WebRequest request
    ) {
        log.warn("이미 존재하는 예산입니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(409L).build(),
                HttpStatus.CONFLICT);
    }
}
