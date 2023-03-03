package com.uliga.uliga_backend.domain.Member.exception.handler;

import com.uliga.uliga_backend.domain.Member.exception.EmailCertificationExpireException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(EmailCertificationExpireException.class)
    protected final ResponseEntity<ErrorResponse> handleEmailCertificationExpireException(
            EmailCertificationExpireException ex, WebRequest request
    ) {
        log.info("만료된 이메일 인증 코드입니다");
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(HttpStatus.CONFLICT)
                        .message(ex.getMessage())
                        .build()
                , HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MessagingException.class)
    protected final ResponseEntity<ErrorResponse> handleMessagingException(
            MessagingException ex, WebRequest request
    ) {
        log.info("이메일 전송중 오류가 발생했습니다");
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(ex.getMessage()
                        ).build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
