package com.uliga.uliga_backend.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {
  // SecurityContext에 유저 정보가 저장되는 시점
  // Request가 들어올때 JwtFilter의 doFilter에서 저장
  // public static Long getCurrentMemberId() {
  // Authentication authentication =
  // SecurityContextHolder.getContext().getAuthentication();
  // if (authentication == null || authentication.getName() == null) {
  // throw new NotAuthorizedException();
  // }

  // return Long.parseLong(authentication.getName());
  // }

}
