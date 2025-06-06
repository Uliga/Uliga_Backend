package com.uliga.uliga_backend.security.oauth2.exception;

public class NotInitializedInception extends IllegalArgumentException {
  public NotInitializedInception() {
    super("사용자가 초기화되지 않았습니다");
  }

  public NotInitializedInception(String s) {
    super(s);
  }
}
