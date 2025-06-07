package com.uliga.uliga_backend.member.exception;

public class LogoutMemberException extends IllegalArgumentException {
  public LogoutMemberException() {
    super("이미 로그아웃한 유저입니다");
  }

  public LogoutMemberException(String s) {
    super(s);
  }
}
