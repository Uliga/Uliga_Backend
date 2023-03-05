package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import com.uliga.uliga_backend.domain.Token.exception.ExpireRefreshTokenException;
import com.uliga.uliga_backend.domain.Token.exception.InvalidRefreshTokenException;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
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

import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static com.uliga.uliga_backend.domain.Token.dto.TokenDTO.*;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public String signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);
        // 현재 가계부 생성은 안 해놓음, 필요하면 추가할 예정

        return "CREATED";
    }

    @Transactional
    public LoginResult login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return LoginResult.builder()
                .memberInfo(memberRepository.findMemberInfoById(Long.parseLong(authenticate.getName())))
                .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                .build();
    }

    @Transactional
    public TokenIssueDTO reissue(AccessTokenDTO accessTokenDTO) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshByAccess = valueOperations.get(accessTokenDTO.getAccessToken());
        if (refreshByAccess == null) {
            throw new ExpireRefreshTokenException();
        }
        // refresh token 검증
        if (!jwtTokenProvider.validateToken(refreshByAccess)) {
            throw new InvalidRefreshTokenException();
        }

        // Access Token에서 멤버 아이디 가져오기
        Authentication authentication = jwtTokenProvider.getAuthentication(accessTokenDTO.getAccessToken());

        // 새로운 토큰 생성
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication);
        // 저장소 정보 업데이트
        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(tokenInfoDTO.getAccessToken(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);


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
}
