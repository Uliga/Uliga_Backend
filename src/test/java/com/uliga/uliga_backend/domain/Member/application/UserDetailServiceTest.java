package com.uliga.uliga_backend.domain.Member.application;

import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserDetailServiceTest {
    @Autowired
    UserDetailService userDetailService;
    @Autowired
    AuthService authService;

     SignUpRequest createSignUpRequest(String email, String nickname) {
        return SignUpRequest.builder()
                .email(email)
                .applicationPassword("1234")
                .nickName(nickname)
                .password("12345678")
                .userName("testUser")
                .build();
    }

    @Test
    @DisplayName("이메일로 UserDetail 조회 성공")
     void getUserDetailByEmailToSuccess() throws Exception{
        // given
        SignUpRequest signUpRequest = createSignUpRequest("email@email.com", "testNickname");

        // when
        Long aLong = authService.signUp(signUpRequest);

        // then
        UserDetails userDetails = userDetailService.loadUserByUsername("email@email.com");
        assertEquals(Long.parseLong(userDetails.getUsername()), aLong);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 userDetail 조회 실패")
     void getUserDetailByEmailToFail() throws Exception{
        // given
        SignUpRequest signUpRequest = createSignUpRequest("email@email.com", "testNickname");

        // when
        Long aLong = authService.signUp(signUpRequest);

        // then
        assertThrows(UsernameNotFoundException.class, () -> userDetailService.loadUserByUsername("email2@email.com"));
    }

}