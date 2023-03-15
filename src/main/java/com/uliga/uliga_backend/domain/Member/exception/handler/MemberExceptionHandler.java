package com.uliga.uliga_backend.domain.Member.exception.handler;

import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SearchEmailResult;
import com.uliga.uliga_backend.domain.Member.exception.*;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                        .errorCode(409L)
                        .message("만료돤 이메일 인증 코드입니다.")
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
                        .errorCode(500L)
                        .message("이메일 전송중 오류가 발생했습니다.")
                        .build()
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidApplicationPasswordException.class)
    protected final ResponseEntity<ErrorResponse> handleInvalidApplicationPasswordException(
            InvalidApplicationPasswordException ex, WebRequest request
    ) {
        log.info("애플리케이션 비밀번호가 잘못되었습니다");
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message("애플리케이션 비밀번호가 잘못되었습니다.").build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected final ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request
    ) {
        log.info(ex.getLocalizedMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message("잘못된 값이 들어왔습니다. 값 형식을 확인해주세요").build(),
                HttpStatus.CONFLICT
        );

    }

    @ExceptionHandler(UnknownLoginException.class)
    protected final ResponseEntity<ErrorResponse> handleUnknownLoginException(
            UnknownLoginException ex, WebRequest request
    ) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorCode(409L)
                        .message("알 수 없는 로그인 방식으로 온 요청입니다").build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(UserNotFoundByEmail.class)
    protected final ResponseEntity<SearchEmailResult> handleUserNotFoundByEmail(
            UserNotFoundByEmail ex, WebRequest request
    ) {
        log.info(ex.getMessage());
        return ResponseEntity.ok(SearchEmailResult.builder()
                .id(null)
                .nickName("null")
                .nickName("null").build());
    }

    @ExceptionHandler(UserExistsInAccountBook.class)
    protected final ResponseEntity<SearchEmailResult> handleUserExistsInAccountBook(
            UserExistsInAccountBook ex, WebRequest request
    ) {
        log.info(ex.getMessage()+"이미 존재하는 유저입니다");
        return ResponseEntity.ok(SearchEmailResult.builder()
                .id(null)
                .userName(ex.getMessage()+"는/은 이미 존재하는 유저입니다")
                .nickName(ex.getMessage()+"는/은 이미 존재하는 유저입니다").build());
    }

    @ExceptionHandler(CannotLoginException.class)
    protected final ResponseEntity<ErrorResponse> handleCannotLoginException(
            CannotLoginException ex, WebRequest request
    ) {
        log.info("로그인 불가");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorCode(409L).message(ex.getMessage()).build(), HttpStatus.CONFLICT);
    }
}
