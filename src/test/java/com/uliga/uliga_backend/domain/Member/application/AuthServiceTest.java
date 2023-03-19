package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AuthServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AccountBookRepository accountBookRepository;

    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    public SignUpRequest createSignUpRequest(String email, String nickname) {
        return SignUpRequest.builder()
                .email(email)
                .applicationPassword("1234")
                .nickName(nickname)
                .password("12345678")
                .userName("testUser")
                .build();
    }

    public LoginRequest createLoginRequest(String email) {
        return LoginRequest.builder()
                .email(email)
                .password("12345678").build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void signUpTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("test@email.com", "nickname");

        // when
        authService.signUp(signUpRequest);

        // then
        assertTrue(memberRepository.existsByEmail("test@email.com"));

    }

    @Test
    @DisplayName("회원가입 시 가계부 생성 확인 테스트")
    public void signUpTestToSuccessCheckAccountBook() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nickname");


        // when
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail("nouser@email.com").orElseThrow(NotFoundByIdException::new);
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(member.getId());

        // then
        assertEquals(1, accountBookInfosByMemberId.size());

    }

    @Test
    @DisplayName("회원 가입 시 가계부 카테고리 생성 확인 테스트")
    public void signUpTestToSuccessCheckAccountBookCategory() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nickname");


        // when
        authService.signUp(signUpRequest);
        Member member = memberRepository.findByEmail("nouser@email.com").orElseThrow(NotFoundByIdException::new);
        List<AccountBookCategoryInfoQ> accountBookCategoryInfoById = accountBookRepository.findAccountBookCategoryInfoById(member.getPrivateAccountBook().getId());

        // then
        assertAll(
                ()-> assertEquals(accountBookCategoryInfoById.size(), defaultCategories.size()),
                () -> {
                    for (AccountBookCategoryInfoQ accountBookCategoryInfoQ : accountBookCategoryInfoById) {
                        assertTrue(defaultCategories.contains(accountBookCategoryInfoQ.getName()));
                    }
                }
        );
    }

    @Test
    @DisplayName("이메일 중복 확인 테스트")
    public void duplicateEmailCheckTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nickname");


        // when
        authService.signUp(signUpRequest);
        // then
        assertTrue(authService.emailExists("nouser@email.com").isExists());

    }

    @Test
    @DisplayName("존재하지 않는 이메일 확인 테스트")
    public void duplicateEmailCheckTestToFail() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nickname");

        // when
        authService.signUp(signUpRequest);
        // then
        assertFalse(authService.emailExists("nouser1@email.com").isExists());
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    public void duplicateNicknameCheckTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nouser");


        // when
        authService.signUp(signUpRequest);

        // then
        assertTrue(authService.nicknameExists("nouser").isExists());
    }

    @Test
    @DisplayName("존재하지 않는 닉네임 확인 테스트")
    public void duplicateNicknameCheckTestToFail() throws Exception{
        //given
        SignUpRequest signUpRequest= createSignUpRequest("nouser@email.com", "nouser");


        // when
        authService.signUp(signUpRequest);

        // then
        assertFalse(authService.nicknameExists("nouser1").isExists());
    }


}