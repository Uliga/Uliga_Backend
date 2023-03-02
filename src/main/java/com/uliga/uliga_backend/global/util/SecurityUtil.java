package com.uliga.uliga_backend.global.util;

import com.uliga.uliga_backend.domain.Member.exception.NotAuthorizedException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@NoArgsConstructor
public class SecurityUtil {
    // SecurityContext에 유저 정보가 저장되는 시점
    // Request가 들어올때 JwtFilter의 doFilter에서 저장
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new NotAuthorizedException();
        }

        return Long.parseLong(authentication.getName());
    }

    /*
        회원의 경우 게시글을 조회할때 본인이 누른 스크랩 등을 확인할 수 있어야 한다.
        따라서 현재 게시글을 조회하는 사용자가 로그인된 회원이라면 아이디를 반환해주고,
        비회원이라면 null을 반환해줄 필요성이 있기에 해당 메소드를 만들었다.
     */
    public static Long getCurrentNullableMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            return null;
        }
        return Long.parseLong(authentication.getName());
    }
}
