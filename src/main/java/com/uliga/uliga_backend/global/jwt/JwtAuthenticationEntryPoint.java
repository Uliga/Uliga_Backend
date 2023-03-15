package com.uliga.uliga_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info(String.valueOf(authException.getClass()));
        log.info(authException.getMessage());
        log.info(authException.getLocalizedMessage());
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

        }  else if (authException instanceof InsufficientAuthenticationException){
            result = objectMapper.writeValueAsString(new ErrorResponse(401L, "유효하지 않은 엑세스 토큰입니다."));
            response.setStatus(response.SC_UNAUTHORIZED);
        } else {
            result = objectMapper.writeValueAsString(new ErrorResponse(500L,
                    "서버 내부 오류가 발생하였습니다"));
            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
        }




        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
        response.getWriter().write(result);

    }
}


