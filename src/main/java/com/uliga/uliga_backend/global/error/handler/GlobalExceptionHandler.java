package com.uliga.uliga_backend.global.error.handler;

import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.InvalidDataValueException;
import com.uliga.uliga_backend.global.error.exception.NotAuthorizedException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import io.lettuce.core.RedisCommandTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        log.warn("해당 아이디로 존재하는 객체를 찾을 수 없습니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(404L)
                .message(ex.getMessage())
                .build()
                , NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    protected final ResponseEntity<ErrorResponse> handleNotAuthorizedException(
            NotAuthorizedException ex, WebRequest request
    ) {
        log.warn("접근 권한이 없는 요청입니다");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(401L)
                .message("접근 권한이 없는 요청입니다.")
                .build()
                , UNAUTHORIZED);
    }


    @ExceptionHandler(RedisCommandTimeoutException.class)
    protected final ResponseEntity<ErrorResponse> handleRedisTimeoutException(
            RedisCommandTimeoutException ex, WebRequest request
    ) {
        log.warn("레디스 타임아웃 뜸");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(504L)
                .message("쉬는 시간 당첨 쉬다 오세여")
                .build(), GATEWAY_TIMEOUT);
    }

    @ExceptionHandler(IdNotFoundException.class)
    protected final ResponseEntity<ErrorResponse> handleIdNotFoundException(
            IdNotFoundException ex, WebRequest request
    ) {
        log.warn("아이디가 없어요");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(404L)
                .message(ex.getMessage()).build()
                , NOT_FOUND);
    }

    @ExceptionHandler(InvalidDataValueException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidDataValue(
            InvalidDataValueException ex, WebRequest request
    ) {
        log.warn(ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L)
                .message(ex.getMessage()).build()
                , CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request
    ) {
        log.warn(ex.getLocalizedMessage());
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message(builder.toString()).build(),
                HttpStatus.CONFLICT
        );

    }
}