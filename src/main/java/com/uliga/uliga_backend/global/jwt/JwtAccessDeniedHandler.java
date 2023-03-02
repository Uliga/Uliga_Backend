package com.uliga.uliga_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.global.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendResponse(response, accessDeniedException);
    }

    private void sendResponse(HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        String result = objectMapper.writeValueAsString(new ErrorResponse(FORBIDDEN, accessDeniedException.getMessage()));


        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(response.SC_FORBIDDEN);
    }
}
