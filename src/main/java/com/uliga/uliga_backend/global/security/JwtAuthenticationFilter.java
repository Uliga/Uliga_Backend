package com.uliga.uliga_backend.global.security;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;

import com.uliga.uliga_backend.global.security.jwt.JwtTokenProvider;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            RedisTemplate<String, String> redisTemplate) {
        // Pass the authentication manager that will produce an AuthenticationToken
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisTemplate = redisTemplate;
        // Only apply this filter to all paths
        this.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        // Convert incoming request into AuthenticationToken
        this.setServerAuthenticationConverter(new JwtServerAuthenticationConverter());
    }

    private class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String token = resolveToken(exchange.getRequest());
            if (token == null) {
                return Mono.empty(); // No token in header, skip authentication
            }

            // 1. Signature and expiration validation
            if (!jwtTokenProvider.validateToken(token)) {
                return Mono.error(new BadCredentialsException("Invalid or expired JWT token"));
            }

            // 2. Redis blacklist check (e.g., logout handling)
            String redisKey = "blacklist:" + token;
            return Mono.fromCallable(() -> redisTemplate.hasKey(redisKey))
                    .flatMap(isBlacklisted -> {
                        if (Boolean.TRUE.equals(isBlacklisted)) {
                            return Mono.error(new BadCredentialsException("JWT token is blacklisted"));
                        }
                        // 3. Extract claims
                        Claims claims = jwtTokenProvider.getClaims(token);
                        String userId = claims.getSubject();
                        // 4. Build authorities from claims
                        Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                        // 5. Create AuthenticationToken
                        return Mono.just(new UsernamePasswordAuthenticationToken(userId, token, authorities));
                    });
        }

        private String resolveToken(ServerHttpRequest request) {
            String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        }
    }
}
