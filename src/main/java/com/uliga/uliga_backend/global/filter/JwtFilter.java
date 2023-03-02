package com.uliga.uliga_backend.global.filter;

import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.uliga.uliga_backend.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.BEARER_PREFIX;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. request Header에서 토큰 꺼냄, 여기서 HTTP ONLY 쿠키에서 읽어오게 변경 가능
        String jwt = resolveToken(request);
        // 2. validateToken으로 유효성 검사
        // 정상 토큰이면, Authentication을 가져와서 SecurityContext에 저장
        if (jwt != null) {
            Boolean hasKey = redisTemplate.hasKey(jwt);

            if (hasKey == null) {
                hasKey = false;
            }
            if (StringUtils.hasText(jwt) && hasKey && jwtTokenProvider.validateToken(jwt)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
