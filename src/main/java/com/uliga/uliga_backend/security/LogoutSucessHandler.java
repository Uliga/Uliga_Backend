package com.uliga.uliga_backend.security;

import java.net.URI;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.uliga.uliga_backend.security.jwt.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Reactive logout success handler for WebFlux, blacklisting the JWT in Redis
 * and redirecting.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class LogoutSucessHandler implements ServerLogoutSuccessHandler {

  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
    ServerWebExchange exchange = webFilterExchange.getExchange();
    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      if (jwtTokenProvider.validateToken(token)) {
        Claims claims = jwtTokenProvider.getClaims(token);
        String userId = claims.getSubject();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if (ops.get(userId) != null) {
          ops.getAndDelete(userId);
        }
      }
    }
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.FOUND);
    response.getHeaders().setLocation(URI.create("https://api.ouruliga.com/auth/logout-redirect"));
    return response.setComplete();
  }
}