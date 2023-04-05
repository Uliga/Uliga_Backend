package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateRequestPrivate;
import com.uliga.uliga_backend.domain.Member.dao.MemberMapper;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import com.uliga.uliga_backend.domain.Token.exception.ExpireRefreshTokenException;
import com.uliga.uliga_backend.domain.Token.exception.InvalidRefreshTokenException;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import com.uliga.uliga_backend.global.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final AccountBookService accountBookService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    @Transactional
    public Long signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);

        CreateRequestPrivate requestPrivate = CreateRequestPrivate.builder().name(member.getUserName() + " 님의 가계부").relationship("개인").isPrivate(true).build();
        accountBookService.createAccountBookPrivate(member, requestPrivate);
        return member.getId();
    }

    @Transactional
    public void socialSignUp(SocialSignUpRequest socialSignUpRequest) {
        socialSignUpRequest.encrypt(passwordEncoder);
        Member member = socialSignUpRequest.toEntity();
        memberRepository.save(member);

        CreateRequestPrivate build = CreateRequestPrivate.builder().name(member.getUserName() + " 님의 가계부").relationship("개인 가계부").isPrivate(true).build();
        accountBookService.createAccountBookPrivate(member, build);


    }

    @Transactional
    public LoginResult login(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME);
        redisTemplate.expire(authenticate.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return LoginResult.builder()
                .memberInfo(memberRepository.findMemberInfoById(Long.parseLong(authenticate.getName())))
                .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                .build();
    }

    @Transactional
    public TokenIssueDTO reissue(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = CookieUtil.getCookie(request, ACCESS_TOKEN).orElse(null);
        String accessToken;
        if (cookie == null) {
            throw new ExpireRefreshTokenException();
        } else {
            accessToken = cookie.getValue();
        }
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // Access Token에서 멤버 아이디 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String refreshByAccess = valueOperations.get(authentication.getName());
        if (refreshByAccess == null) {
            log.info("토큰 재발급 API 중 리프레쉬 만료 확인");
            throw new ExpireRefreshTokenException();
        }
        // refresh token 검증
        if (!jwtTokenProvider.validateToken(refreshByAccess)) {
            log.info("토큰 재발급 API 중 유효하지 않은 리프레쉬 확인");
            throw new InvalidRefreshTokenException();
        }


        // 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication);
        // 저장소 정보 업데이트
        log.info("토큰 재발급 성공후 레디스에 값 저장");
        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN);
        CookieUtil.addCookie(response, ACCESS_TOKEN, tokenInfoDTO.getAccessToken(), ACCESS_TOKEN_COOKIE_EXPIRE_TIME);
        valueOperations.set(authentication.getName(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authentication.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);


        // 토큰 발급
        return tokenInfoDTO.toTokenIssueDTO();
    }

    @Transactional
    public ExistsCheckDto emailExists(String email) {
        return ExistsCheckDto.builder()
                .exists(memberRepository.existsByEmail(email)).build();
    }

    @Transactional
    public ExistsCheckDto nicknameExists(String nickname) {
        return ExistsCheckDto.builder()
                .exists(memberRepository.existsByNickName(nickname)).build();
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

    }

}
