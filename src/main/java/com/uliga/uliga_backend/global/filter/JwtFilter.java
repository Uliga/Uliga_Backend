package com.uliga.uliga_backend.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Token.exception.ExpireTokenException;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.server.ExportException;

import static com.uliga.uliga_backend.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.BEARER_PREFIX;
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;


    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {
            // 1. request Header에서 토큰 꺼냄, 여기서 HTTP ONLY 쿠키에서 읽어오게 변경 가능
            String jwt = resolveToken(request);
            // 2. validateToken으로 유효성 검사
            // 정상 토큰이면, Authentication을 가져와서 SecurityContext에 저장
            if (jwt != null) {
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    User user = (User) authentication.getPrincipal();
                    if (user.getUsername() != null && redisTemplate.hasKey(user.getUsername())) {
                        log.info("memberId : " + user.getUsername());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        throw new ExpireTokenException("리프레쉬 만료");
                    }

                } else {
                    throw new ExpireTokenException("엑세스 만료");
                }
            }


            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.info(e.getMessage());
            log.info(e.getClass().getName());
            String result = mapper.writeValueAsString(new ErrorResponse(401L, e.getMessage()));
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(response.SC_UNAUTHORIZED);
            response.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
            try {
                response.getWriter().write(result);
            } catch (IOException exception) {
                e.printStackTrace();
            }

        }

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


}
