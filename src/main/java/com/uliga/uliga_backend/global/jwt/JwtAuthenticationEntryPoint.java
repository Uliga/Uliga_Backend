package com.uliga.uliga_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.exception.LogoutMemberException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info(String.valueOf(authException.getClass()));
        log.info(authException.getMessage());
        sendResponse(response, authException);
    }

    private void sendResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {
        String result;
        if (authException instanceof BadCredentialsException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));
            response.setStatus(response.SC_CONFLICT);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404L,
                    "존재하지 않는 멤버입니다."));
            response.setStatus(response.SC_NOT_FOUND);

        } else if (authException instanceof InsufficientAuthenticationException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404L,
                    "인증 조건을 충족하지 않은 요청입니다"));
            response.setStatus(response.SC_BAD_REQUEST);
        } else {
            result = objectMapper.writeValueAsString(new ErrorResponse(401L, "유효하지 않은 엑세스 토큰입니다."));
            response.setStatus(response.SC_UNAUTHORIZED);
        }



        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.getWriter().write(result);

    }
}


