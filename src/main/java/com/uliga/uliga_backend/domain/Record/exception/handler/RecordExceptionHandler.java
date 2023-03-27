package com.uliga.uliga_backend.domain.Record.exception.handler;

import com.uliga.uliga_backend.domain.Record.exception.InvalidRecordDelete;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class RecordExceptionHandler {
    @ExceptionHandler(InvalidRecordDelete.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidRecordDelete(
            InvalidRecordDelete ex, WebRequest request
    ) {
        log.info("잘못된 지출 삭제 요청");
        return new ResponseEntity<>(ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(409L).build(),
                HttpStatus.CONFLICT);
    }
}

