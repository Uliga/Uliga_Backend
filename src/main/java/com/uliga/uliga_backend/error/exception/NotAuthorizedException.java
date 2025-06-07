package com.uliga.uliga_backend.error.exception;

public class NotAuthorizedException extends IllegalArgumentException {
  public NotAuthorizedException() {
    super("접근 권한이 없습니다.");

  }

  public NotAuthorizedException(String s) {
    super(s);
  }
}
