package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.CodeConfirmDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.ConfirmEmailDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.EmailConfirmCodeDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.exception.CannotLoginException;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberAuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberAuthControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private EmailCertificationService emailCertificationService;

    private static final String BASE_URL = "/auth";

     SignUpRequest createSignUpRequest(String email, String nickname) {
        return SignUpRequest.builder()
                .email(email)
                .applicationPassword("1234")
                .nickName(nickname)
                .password("12345678")
                .userName("testUser")
                .build();
    }

     LoginRequest createLoginRequest(String email) {
        return LoginRequest.builder()
                .email(email)
                .password("12345678").build();
    }


    @Test
    @WithMockCustomUser
    @DisplayName("회원가입 성공 테스트")
     void signUpTestToSuccess() throws Exception{
        // given
        SignUpRequest mvctest = createSignUpRequest("mvctest@email.com", "mvctest");

        // when
        String body = mapper.writeValueAsString(mvctest);
        doReturn(1L).when(authService).signUp(mvctest);
        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @WithMockCustomUser
    @DisplayName("이메일 - 잘못된 값 형식으로 회원가입 실패 테스트")
     void signUpTestToFailByInvalidEmail() throws Exception{
        // given
        SignUpRequest mvctest = createSignUpRequest("mvctestemail.com", "mvctest");

        // when
        String body = mapper.writeValueAsString(mvctest);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
    
    @Test
    @WithMockCustomUser
    @DisplayName("비밀번호 - 잘못된 값 형식으로 회원가입 실패 테스트")
     void signUpTestToFailByInvalidPassword() throws Exception{
        // given
        SignUpRequest build = SignUpRequest.builder()
                .email("mvctest@email.com")
                .applicationPassword("1234")
                .nickName("mvctest")
                .password("1234")
                .userName("testUser")
                .build();

        // when
        String body = mapper.writeValueAsString(build);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("닉네임 - 잘못된 값 형식으로 회원가입 실패 테스트")
     void signUpTestToFailByInvalidNickname() throws Exception{
        // given
        SignUpRequest build = SignUpRequest.builder()
                .email("mvctest@email.com")
                .applicationPassword("1234")
                .nickName("m")
                .password("12345678")
                .userName("testUser")
                .build();

        // when
        String body = mapper.writeValueAsString(build);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("유저네임 - 잘못된 값 형식으로 회원가입 실패 테스트")
     void signUpTestToFailByNullUsername() throws Exception{
        // given
        SignUpRequest build = SignUpRequest.builder()
                .email("mvctest@email.com")
                .applicationPassword("1234")
                .nickName("mvctest")
                .password("12345678")
                .build();

        // when
        String body = mapper.writeValueAsString(build);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("애플리케이션 비밀번호 - 잘못된 값 형식으로 회원가입 실패 테스트")
     void signUpTestToFailByNullApplicationPassword() throws Exception{
        // given
        SignUpRequest build = SignUpRequest.builder()
                .email("mvctest@email.com")
                .nickName("mvctest")
                .password("12345678")
                .userName("testUser")
                .build();

        // when
        String body = mapper.writeValueAsString(build);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .with(csrf())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("로그인 성공 테스트")
     void loginTestToSuccess() throws Exception{
        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("12345678").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);


        // then
        mvc.perform(post(BASE_URL + "/login")
                        .with(csrf())
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("잘못된 이메일 - 로그인 실패")
     void loginTestToFailByInvalidEmail() throws Exception{
        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test1@email.com")
                .password("12345678").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);
        doThrow(CannotLoginException.class).when(authService).login(any(), any(), any());
        String er = mapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));

        // then
        mvc.perform(post(BASE_URL + "/login")
                        .with(csrf())
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString().equals(er);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("잘못된 비밀번호 - 로그인 실패")
     void loginTestToFailByInvalidPassword() throws Exception{
        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("123456789").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);
        doThrow(CannotLoginException.class).when(authService).login(any(), any(), any());
        String er = mapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));

        // then
        mvc.perform(post(BASE_URL + "/login")
                        .with(csrf())
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString().equals(er);
    }

   @Test
   @WithMockCustomUser
   @DisplayName("이메일 전송 성공 테스트")
    void sendEmailTestToSuccess() throws Exception{
       // given
       ConfirmEmailDto email = ConfirmEmailDto.builder().email("dongjunkim99@icloud.com").build();

       // when
       String body = mapper.writeValueAsString(email);
       doNothing().when(emailCertificationService).sendSimpleMessage(any());
       String re = mapper.writeValueAsString(MemberDTO.EmailSentDto.builder().email("dongjunkim99@icloud.com").success(true).build());

       // then
       mvc.perform(post(BASE_URL + "/mail")
                       .with(csrf())
                       .content(body)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn().getResponse().getContentAsString().equals(re);
   }

    @Test
    @WithMockCustomUser
    @DisplayName("코드 검증 성공 테스트")
     void confirmCodeTestToSuccess() throws Exception{
        // given
        EmailConfirmCodeDto emailConfirmCodeDto = EmailConfirmCodeDto.builder()
                .code("random").email("dongjunkim99@icloud.com").build();
        CodeConfirmDto codeConfirmDto = CodeConfirmDto.builder().matches(true).build();

        // when
        String body = mapper.writeValueAsString(emailConfirmCodeDto);
        doReturn(codeConfirmDto).when(emailCertificationService).confirmCode(emailConfirmCodeDto);
        String res = mapper.writeValueAsString(codeConfirmDto);

        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(res);
    }
}