package com.uliga.uliga_backend.global.error;

import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundByIdException.class)
    protected final ResponseEntity<String> handleNotFoundByIdException(
            NotFoundByIdException ex, WebRequest request) {
        log.debug("해당 아이디로 존재하는 객체를 찾을 수 없습니다");
        return new ResponseEntity<>("해당 아이디로 존재하는 객체를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
    }
}