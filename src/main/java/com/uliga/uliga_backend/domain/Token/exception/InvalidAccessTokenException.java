package com.uliga.uliga_backend.domain.Token.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidAccessTokenException extends AuthenticationException {

    public InvalidAccessTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidAccessTokenException(String msg) {
        super(msg);
    }
}
