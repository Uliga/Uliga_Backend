package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateRequest;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.CreateRequestPrivate;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO.TokenIssueDTO;
import com.uliga.uliga_backend.domain.Token.exception.ExpireRefreshTokenException;
import com.uliga.uliga_backend.domain.Token.exception.InvalidRefreshTokenException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static com.uliga.uliga_backend.domain.Token.dto.TokenDTO.*;
import static com.uliga.uliga_backend.global.common.constants.JwtConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final AccountBookService accountBookService;
    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    @Transactional
    public String signUp(SignUpRequest signUpRequest) {
        signUpRequest.encrypt(passwordEncoder);
        Member member = signUpRequest.toEntity();
        memberRepository.save(member);
        CreateRequestPrivate build = CreateRequestPrivate.builder().name(member.getNickName() + " 님의 가계부").isPrivate(true).build();

        AccountBook accountBook = build.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);
        for (String cat : defaultCategories) {
            Category category = Category.builder()
                    .accountBook(accountBook)
                    .name(cat).build();
            categoryRepository.save(category);
        }

        return "CREATED";
    }

    @Transactional
    public void socialSignUp(SocialSignUpRequest socialSignUpRequest) {
        socialSignUpRequest.encrypt(passwordEncoder);
        Member member = socialSignUpRequest.toEntity();
        memberRepository.save(member);

        CreateRequestPrivate build = CreateRequestPrivate.builder().name(member.getNickName() + " 님의 가계부").isPrivate(true).build();
        accountBookService.createAccountBookPrivate(member.getId(), build);


    }

    @Transactional
    public LoginResult login(LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginRequest.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);
        log.info("로그인 API 중 토큰 생성 로직 실행");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
//        valueOperations.set(tokenInfoDTO.getAccessToken(), tokenInfoDTO.getRefreshToken());
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
}
