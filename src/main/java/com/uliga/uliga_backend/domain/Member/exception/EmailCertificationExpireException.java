package com.uliga.uliga_backend.domain.Member.exception;

public class EmailCertificationExpireException extends IllegalArgumentException {
    public EmailCertificationExpireException() {
        super("이메일 인증 시간이 만료되었습니다.");
    }

    public EmailCertificationExpireException(String s) {
        super(s);
    }
}
