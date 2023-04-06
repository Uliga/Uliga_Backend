package com.uliga.uliga_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.exception.LogoutMemberException;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisTemplate<String, String> redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper objectMapper;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("로그아웃 호출됐음");
        log.info(response.getHeader("Access-Control-Allow-Origin"));
        log.info(request.getHeader("Access-Control-Allow-Origin"));
        // 여기 response에 cors 관련 헤더 허용해준다는 거 넣으면 될듯? 그리고 여기서도 그 값 제거해줄 수 있을 것 같은데

        String token = request.getHeader("Authorization").split(" ")[1];
        log.info("token = " + token);
        Authentication auth = jwtTokenProvider.getAuthentication(token);


        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(auth.getName()) == null) {
            throw new LogoutMemberException();
        }
        User principal = (User) auth.getPrincipal();
        log.info("제발 "+ principal.getUsername());
        log.info("레디스에 비어있을때"+valueOperations.get(principal.getUsername()));
        valueOperations.getAndDelete(principal.getUsername());

        log.info(request.getRequestURI());
        response.addHeader("Access-Control-Allow-Origin","http://localhost:3000");
        super.onLogoutSuccess(request, response, authentication);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return "auth/logout-redirect";
    }
}
