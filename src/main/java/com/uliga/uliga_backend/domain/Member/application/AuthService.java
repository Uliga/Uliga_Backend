package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateRequestPrivate;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.ReissueRequest;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import com.uliga.uliga_backend.domain.Token.exception.ExpireRefreshTokenException;
import com.uliga.uliga_backend.domain.Token.exception.InvalidRefreshTokenException;
import com.uliga.uliga_backend.global.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AccountBookService accountBookService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원가입 메서드
     * @param signUpRequest 회원가입 요청 dto
     * @return 회원가입 결과
     */
    @Transactional
    public Long signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);

        CreateRequestPrivate requestPrivate = CreateRequestPrivate.builder().name(member.getUserName() + " 님의 가계부").relationship("개인").isPrivate(true).build();
        accountBookService.createAccountBookPrivate(member, requestPrivate);
        return member.getId();
    }

    /**
     * 로그인 메서드
     * @param loginRequest 로그인 요청
     * @param response httpServletResponse
     * @param request httpServletRequest
     * @return 로그인 결과
     */
    @Transactional
    public LoginResult login(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authenticate.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return LoginResult.builder()
                .memberInfo(memberRepository.findMemberInfoById(Long.parseLong(authenticate.getName())))
                .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                .build();
    }

    /**
     * 소셜 로그인 회원가입 메서드
     * @param socialLoginRequest 소셜로그인시 회원가입 요청
     * @return 로그인 결과
     */
    @Transactional
    public LoginResult socialLogin(SocialLoginRequest socialLoginRequest) {
        Member entity = socialLoginRequest.toEntity(passwordEncoder);
        memberRepository.save(entity);
        CreateRequestPrivate requestPrivate = CreateRequestPrivate.builder().name(socialLoginRequest.getUserName() + " 님의 가계부").relationship("개인").isPrivate(true).build();
        accountBookService.createAccountBookPrivate(entity, requestPrivate);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = socialLoginRequest.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authenticate.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return LoginResult.builder()
                .memberInfo(memberRepository.findMemberInfoById(Long.parseLong(authenticate.getName())))
                .tokenInfo(tokenInfoDTO.toTokenIssueDTO())
                .build();
    }

    /**
     * 토큰 재발급 메서드
     * @param reissueRequest 재발급 요청
     * @return 토큰 재발급 결과
     */
    @Transactional
    public TokenIssueDTO reissue(ReissueRequest reissueRequest) {

        String accessToken = reissueRequest.getToken();

        log.info("access : " + accessToken);
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
        valueOperations.set(authentication.getName(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authentication.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);


        // 토큰 발급
        return tokenInfoDTO.toTokenIssueDTO();
    }

    /**
     * 이메일 중복 조회
     * @param email 중복 조회할 이메일
     * @return 중복 조회 결과
     */
    @Transactional(readOnly = true)
    public ExistsCheckDto emailExists(String email) {
        Optional<Member> byEmailAndDeleted = memberRepository.findByEmailAndDeleted(email, false);
        if (byEmailAndDeleted.isPresent()) {
            Member member = byEmailAndDeleted.get();
            return ExistsCheckDto.builder().exists(true).loginType(member.getUserLoginType()).build();
        } else {
            return ExistsCheckDto.builder().loginType(UserLoginType.EMAIL).exists(false).build();
        }

    }

    /**
     * 닉네임 중복 조회
     * @param nickname 중복 조회할 닉네임
     * @return 중복 조회 결과
     */
    @Transactional(readOnly = true)
    public ExistsCheckDto nicknameExists(String nickname) {
        return ExistsCheckDto.builder()
                .exists(memberRepository.existsByNickNameAndDeleted(nickname, false)).build();
    }



}
