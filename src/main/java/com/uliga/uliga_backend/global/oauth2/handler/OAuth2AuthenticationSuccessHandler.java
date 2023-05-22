package com.uliga.uliga_backend.global.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import com.uliga.uliga_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.uliga.uliga_backend.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;
import static com.uliga.uliga_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("authentication success");



        log.info("authentication : "+authentication.getName());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Optional<Member> byId = memberRepository.findById(Long.parseLong(authentication.getName()));

        if (response.isCommitted()) {
            return;
        }
        String targetUrl = determineTargetUrl(request, response, authentication, byId);
        log.info("target url : "+targetUrl);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication, Optional<Member> byId) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        //토큰 생성
        TokenInfoDTO tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // redis에 쿠키 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (byId.isPresent()) {
            Member member = byId.get();
            if (member.getApplicationPassword() == null) {
                if (member.getNickName() == null) {
                    UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl)
                            .queryParam("email", member.getEmail())
                            .queryParam("created", true)
                            .queryParam("nickname","null")
                            .queryParam("applicationPassword", "null")
                            .build();
                    return uriComponents.toUriString();
                } else {
                    UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl)
                            .queryParam("email", member.getEmail())
                            .queryParam("created", true)
                            .queryParam("applicationPassword", "null")
                            .queryParam("nickname", URLEncoder.encode(member.getNickName(), StandardCharsets.UTF_8))
                            .build();
                    return uriComponents.toUriString();
                }
            } else {
                UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam("token", tokenDto.getAccessToken())
                        .queryParam("email", member.getEmail())
                        .queryParam("created", false)
                        .build();

                valueOperations.set(authentication.getName(), tokenDto.getRefreshToken());
                redisTemplate.expire(authentication.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
                return uriComponents.toUriString();
            }


        } else {
            UriComponents uriComponents = UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("email", "NULL")
                    .build();
            return uriComponents.toUriString();
        }


    }
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


}
