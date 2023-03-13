package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.UligaBackendApplication;
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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
@ContextConfiguration(classes = UligaBackendApplication.class)
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
        SignUpRequest mvctest = createSignUpRequest("mvctest@email.com", "mvctest");

        // when
        String body = mapper.writeValueAsString(mvctest);
        
        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("이메일 - 잘못된 값 형식으로 회원가입 실패 테스트")
    public void signUpTestToFailByInvalidEmail() throws Exception{
        //given
        SignUpRequest mvctest = createSignUpRequest("mvctestemail.com", "mvctest");

        // when
        String body = mapper.writeValueAsString(mvctest);

        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
    
    @Test
    @DisplayName("비밀번호 - 잘못된 값 형식으로 회원가입 실패 테스트")
    public void signUpTestToFailByInvalidPassword() throws Exception{
        //given
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
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("닉네임 - 잘못된 값 형식으로 회원가입 실패 테스트")
    public void signUpTestToFailByInvalidNickname() throws Exception{
        //given
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
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("유저네임 - 잘못된 값 형식으로 회원가입 실패 테스트")
    public void signUpTestToFailByNullUsername() throws Exception{
        //given
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
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("애플리케이션 비밀번호 - 잘못된 값 형식으로 회원가입 실패 테스트")
    public void signUpTestToFailByNullApplicationPassword() throws Exception{
        //given
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
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginTestToSuccess() throws Exception{
        //given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("12345678").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);


        // then
        mvc.perform(post(BASE_URL + "/login")
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 이메일 - 로그인 실패")
    public void loginTestToFailByInvalidEmail() throws Exception{
        //given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test1@email.com")
                .password("12345678").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);
        doThrow(CannotLoginException.class).when(authService).login(any(), any(), any());
        String er = mapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));

        // then
        mvc.perform(post(BASE_URL + "/login")
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString().equals(er);
    }

    @Test
    @DisplayName("잘못된 비밀번호 - 로그인 실패")
    public void loginTestToFailByInvalidPassword() throws Exception{
        //given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test@email.com")
                .password("123456789").build();

        // when
        String s = mapper.writeValueAsString(loginRequest);
        doThrow(CannotLoginException.class).when(authService).login(any(), any(), any());
        String er = mapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));

        // then
        mvc.perform(post(BASE_URL + "/login")
                        .content(s)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn().getResponse().getContentAsString().equals(er);
    }

   @Test
   @DisplayName("이메일 전송 성공 테스트")
   public void sendEmailTestToSuccess() throws Exception{
       //given
       ConfirmEmailDto email = ConfirmEmailDto.builder().email("dongjunkim99@icloud.com").build();

       // when
       String body = mapper.writeValueAsString(email);
       doNothing().when(emailCertificationService).sendSimpleMessage(any());
       String re = mapper.writeValueAsString(MemberDTO.EmailSentDto.builder().email("dongjunkim99@icloud.com").success(true).build());

       // then
       mvc.perform(post(BASE_URL + "/mail")
                       .content(body)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn().getResponse().getContentAsString().equals(re);
   }

    @Test
    @DisplayName("코드 검증 성공 테스트")
    public void confirmCodeTestToSuccess() throws Exception{
        //given
        EmailConfirmCodeDto emailConfirmCodeDto = EmailConfirmCodeDto.builder()
                .code("random").email("dongjunkim99@icloud.com").build();
        CodeConfirmDto codeConfirmDto = CodeConfirmDto.builder().matches(true).build();

        // when
        String body = mapper.writeValueAsString(emailConfirmCodeDto);
        doReturn(codeConfirmDto).when(emailCertificationService).confirmCode(emailConfirmCodeDto);
        String res = mapper.writeValueAsString(codeConfirmDto);

        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(res);
    }
}