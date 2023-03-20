package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.global.error.exception.NotAuthorizedException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberServiceTest {
    @Autowired
    AuthService authService;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public SignUpRequest createSignUpRequest(String email, String nickname) {
        return SignUpRequest.builder()
                .email(email)
                .applicationPassword("1234")
                .nickName(nickname)
                .password("12345678")
                .userName("testUser")
                .build();
    }

    public Map<String, Object> createUpdateApplicationPassword(String applicationPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("applicationPassword", applicationPassword);
        return map;
    }

    public Map<String, Object> createUpdatePassword(String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        return map;
    }

    public Map<String, Object> createUpdateNickname(String nickname) {
        Map<String, Object> map = new HashMap<>();
        map.put("nickName", nickname);
        return map;
    }

    public Map<String, Object> createUpdateAvatarUrl(String avatarUrl) {
        Map<String, Object> map = new HashMap<>();
        map.put("avatarUrl", avatarUrl);
        return map;
    }


    public NicknameCheckDto createNicknameCheck(String nickname) {
        return NicknameCheckDto.builder()
                .nickname(nickname).build();
    }



    public SearchMemberByEmail createSearchMemberByEmail(String email) {
        return SearchMemberByEmail.builder()
                .email(email).build();
    }
    @Test
    @DisplayName("아이디로 멤버 정보 조회 테스트")
    public void getCurrentMemberInfoTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        MemberInfoNativeQ memberInfoById = memberRepository.findMemberInfoById(signUp);

        // then
        assertAll(
                () -> assertEquals(memberInfoById.getEmail(), "nouser@email.com"),
                () -> assertEquals(memberInfoById.getNickName(), "nouser"),
                () -> assertEquals(memberInfoById.getUserName(), "testUser")
        );
    }
    @Test
    @DisplayName("멤버 애플리케이션 비밀번호 일치 테스트")
    public void checkApplicationPasswordTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        Member member = memberRepository.findById(signUp).orElseThrow(NotFoundByIdException::new);

        // then
        assertTrue(passwordEncoder.matches("1234", member.getApplicationPassword()));
    }

    @Test
    @DisplayName("멤버 애플리케이션 비밀번호 불일치 테스트")
    public void checkApplicationPasswordTestToFail() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        Member member = memberRepository.findById(signUp).orElseThrow(NotFoundByIdException::new);

        // then
        assertFalse(passwordEncoder.matches("12345", member.getApplicationPassword()));
    }
    // TODO
    @Test
    @DisplayName("멤버 애플리케이션 비밀번호 업데이트 테스트")
    public void updateApplicationPasswordTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        Map<String, Object> updateApplicationPassword = createUpdateApplicationPassword("12345");
        // when
        memberService.updateMemberInfo(signUp, updateApplicationPassword);
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);

        // then
        assertTrue(passwordEncoder.matches("12345", member.getApplicationPassword()));
    }

    @Test
    @DisplayName("멤버 비밀번호 일치 성공 테스트")
    public void checkMemberPasswordTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);
        // then
        assertTrue(passwordEncoder.matches("12345678", member.getPassword()));
    }

    @Test
    @DisplayName("멤버 비밀번호 일치 실패 테스트")
    public void checkMemberPasswordTestToFail() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);

        // then
        assertFalse(passwordEncoder.matches("123456789", member.getPassword()));
    }
    // TODO
    @Test
    @DisplayName("멤버 비밀번호 업데이트 성공 테스트")
    public void updateMemberPasswordToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);
        Map<String, Object> updatePassword = createUpdatePassword("123456789");
        // when
        memberService.updateMemberInfo(signUp, updatePassword);
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);
        // then
        assertTrue(passwordEncoder.matches("123456789", member.getPassword()));
    }
    // TODO
    @Test
    @DisplayName("멤버 프사 변경 테스트")
    public void updateAvatarUrlTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");
        Long signUp = authService.signUp(signUpRequest);

        // when
        Map<String, Object> newUrl = createUpdateAvatarUrl("newUrl");
        memberService.updateMemberInfo(signUp, newUrl);
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);
        // then
        assertEquals("newUrl", member.getAvatarUrl());
    }

    @Test
    @DisplayName("닉네임 존재하는지 확인 테스트 - 닉네임 존재")
    public void nicknameExistsTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");

        Long signUp = authService.signUp(signUpRequest);
        // when
        NicknameCheckDto nouser = createNicknameCheck("nouser");

        // then
        assertTrue(memberService.nicknameExists(signUp, nouser));
    }
    @Test
    @DisplayName("닉네임 존재하는지 확인 테스트 - 닉네임 존재 X")
    public void nicknameExistsTestToFail() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");

        Long signUp = authService.signUp(signUpRequest);
        // when
        NicknameCheckDto nouser = createNicknameCheck("nouser1");

        // then
        assertFalse(memberService.nicknameExists(signUp, nouser));
    }
    // TODO
    @Test
    @DisplayName("멤버 닉네임 업데이트 테스트")
    public void updateNicknameTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");

        Long signUp = authService.signUp(signUpRequest);

        Map<String, Object> newNickname = createUpdateNickname("newNickname");
        // when
        memberService.updateMemberInfo(signUp, newNickname);
        Member member = memberRepository.findById(signUp).orElseThrow(NotAuthorizedException::new);
        // then
        assertEquals("newNickname", member.getNickName());
    }

    @Test
    @DisplayName("이메일로 존재하는 멤버 검색 테스트")
    public void memberSearchByEmailTestToSuccess() throws Exception{
        //given
        SignUpRequest signUpRequest = createSignUpRequest("nouser@email.com", "nouser");

        Long signUp = authService.signUp(signUpRequest);

        SearchMemberByEmail searchMemberByEmail = createSearchMemberByEmail("nouser@email.com");
        // when
        SearchEmailResult memberByEmail = memberService.findMemberByEmail(null, searchMemberByEmail);

        // then
        assertAll(
                () -> assertEquals("nouser", memberByEmail.getNickName()),
                () -> assertEquals("testUser", memberByEmail.getUserName())
        );

    }
}