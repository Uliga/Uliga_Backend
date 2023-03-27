package com.uliga.uliga_backend.domain.Schedule.exception.handler;

import com.uliga.uliga_backend.domain.Schedule.exception.InvalidScheduleDelete;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class ScheduleExceptionHandler {
    @ExceptionHandler(InvalidScheduleDelete.class)
    private final ResponseEntity<ErrorResponse> handleInvalidScheduleDelete(
            InvalidScheduleDelete ex, WebRequest request
    ) {
        log.info("유효하지 않은 금융일정 삭제 요청");
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }
}
