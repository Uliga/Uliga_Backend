package com.uliga.uliga_backend.security;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.error.response.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
    var response = exchange.getResponse();
    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    ErrorResponse errorResponse;
    if (authException instanceof BadCredentialsException) {
      status = HttpStatus.CONFLICT;
      errorResponse = new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다.");
    } else if (authException instanceof InternalAuthenticationServiceException) {
      status = HttpStatus.NOT_FOUND;
      errorResponse = new ErrorResponse(404L, "존재하지 않는 멤버입니다.");
    } else if (authException instanceof InsufficientAuthenticationException) {
      status = HttpStatus.SERVICE_UNAVAILABLE;
      errorResponse = new ErrorResponse(503L, "서버 내부 오류가 발생하였습니다");
    } else {
      errorResponse = new ErrorResponse(400L, authException.getMessage());
    }
    response.setStatusCode(status);
    byte[] bytes;
    try {
      bytes = objectMapper.writeValueAsBytes(errorResponse);
    } catch (Exception e) {
      log.error("Error writing authentication entry response", e);
      bytes = ("{\"message\":\"" + authException.getMessage() + "\"}").getBytes(StandardCharsets.UTF_8);
    }
    DataBuffer buffer = response.bufferFactory().wrap(bytes);
    return response.writeWith(Mono.just(buffer));
  }
}
