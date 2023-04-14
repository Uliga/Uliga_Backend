package com.uliga.uliga_backend.global.oauth2.handler;

import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import com.uliga.uliga_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.uliga.uliga_backend.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("authentication success");
        String targetUrl = determineTargetUrl(request, response, authentication);
        if (response.isCommitted()) {
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        log.info("redirect_uri : ");
        //토큰 생성
        TokenInfoDTO tokenDto = jwtTokenProvider.generateTokenDto(authentication);

        // redis에 쿠키 저장
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authentication.getName(), tokenDto.getRefreshToken());
        redisTemplate.expire(authentication.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

//        saveRefreshTokenInStorage(tokenDTO.getRefreshToken(), Long.valueOf(authentication.getName()));
//        CookieUtil.deleteCookie(request,response,ACCESSTOKEN);
//        CookieUtil.addCookie(response,ACCESSTOKEN,tokenDTO.getAccessToken(),  ACCESS_TOKEN_COOKIE_EXPIRE_TIME);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenDto.getAccessToken())
                .build().toUriString();
    }

    /**
     * redis 에 refresh token 저장
     *
//     * @param refreshToken
//     * @param memberId
     */
//    private void saveRefreshTokenInStorage(String refreshToken, Long memberId) {
//        refreshTokenDao.createRefreshToken(memberId, refreshToken);
//    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


}
