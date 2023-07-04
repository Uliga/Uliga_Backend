package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateResult;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.EmailConfirmCodeDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.domain.Member.exception.EmailCertificationExpireException;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.domain.Token.dto.TokenDTO;
import com.uliga.uliga_backend.global.jwt.UserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
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
    private UserDetailService userDetailService;
    @MockBean
    private EmailCertificationService emailCertificationService;
    @MockBean
    private AccountBookService accountBookService;
    @MockBean
    private CategoryService categoryService;

    private static final String BASE_URL = "/auth";

    private static final String EMAIL = "api_test_user@email.com";
    private static final String NICKNAME = "api_test_user";
    private static final String APPLICATION_PASSWORD = "1234";
    private static final String PASSWORD = "12345678";
    private static final String USERNAME = "api_test_user";


    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }


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
    @DisplayName("이메일 인증 성공")
    public void emailCertificationTestToSuccess() throws Exception{
        // given
        EmailConfirmCodeDto confirmCodeDto = EmailConfirmCodeDto.builder().email("email_certificate@email.com").code("123456").build();

        // when
        when(emailCertificationService.confirmCode(any())).thenReturn(MemberDTO.CodeConfirmDto.builder().matches(true).build());
        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(confirmCodeDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/email_certificate/success", requestFields(
                        fieldWithPath("email").description("인증을 요구한 이메일"),
                        fieldWithPath("code").description("이메일로 전송된 인증 코드")
                ), responseFields(
                        fieldWithPath("matches").description("코드 일치 여부")
                )));
    }


    @Test
    @DisplayName("wrong code로 인한 이메일 인증 실패")
    public void emailCertificationTestToFailByWrongCode() throws Exception{
        // given
        EmailConfirmCodeDto confirmCodeDto = EmailConfirmCodeDto.builder().email("email_certificate@email.com").code("123456").build();

        // when
        when(emailCertificationService.confirmCode(any())).thenReturn(MemberDTO.CodeConfirmDto.builder().matches(false).build());
        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(confirmCodeDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/email_certificate/fail/wrong_code", requestFields(
                        fieldWithPath("email").description("인증을 요구한 이메일"),
                        fieldWithPath("code").description("틀린 인증 코드")
                ), responseFields(
                        fieldWithPath("matches").description("코드 일치 여부")
                )));
    }

    @Test
    @DisplayName("expired code로 인한 이메일 인증 실패")
    public void emailCertificationTestToFailByExpiredCode() throws Exception{
        // given
        EmailConfirmCodeDto confirmCodeDto = EmailConfirmCodeDto.builder().email("email_certificate@email.com").code("123456").build();

        // when
        when(emailCertificationService.confirmCode(any())).thenThrow(EmailCertificationExpireException.class);

        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(confirmCodeDto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/email_certificate/fail/expired_code", requestFields(
                        fieldWithPath("email").description("인증을 요구한 이메일"),
                        fieldWithPath("code").description("만료된 코드")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("null code로 인한 이메일 인증 실패")
    public void emailCertificationTestToFailByNullCode() throws Exception{
        // given
        EmailConfirmCodeDto confirmCodeDto = EmailConfirmCodeDto.builder().email("email_certificate@email.com").code("123456").build();

        // when
        when(emailCertificationService.confirmCode(any())).thenThrow(EmailCertificationExpireException.class);

        // then
        mvc.perform(post(BASE_URL + "/mail/code")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(confirmCodeDto)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/email_certificate/fail/null_code", requestFields(
                        fieldWithPath("email").description("인증을 요구한 이메일"),
                        fieldWithPath("code").description("만료된 코드")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }


    @Test
    @DisplayName("이메일 중복 확인 성공 테스트")
    public void emailDuplicateCheckTestToSuccess() throws Exception{
        // when
        when(authService.emailExists(any())).thenReturn(MemberDTO.ExistsCheckDto.builder().exists(false).loginType(UserLoginType.EMAIL).build());
        // then
        mvc.perform(get(BASE_URL + "/mail/exists/testuser@email.com"))
                .andExpect(status().isOk())
                .andDo(document("auth/email_check/success", responseFields(
                        fieldWithPath("exists").description("이메일 존재 여부, false면 존재하지 않는 것"),
                        fieldWithPath("loginType").description("유저가 회원가입한 경로, 이메일, 구글, 카카오 타입이 존재")
                )));
    }

    @Test
    @DisplayName("이메일 중복 확인 실패 테스트")
    public void emailDuplicateCheckTestToFail() throws Exception{
        // when
        when(authService.emailExists(any())).thenReturn(MemberDTO.ExistsCheckDto.builder().exists(true).loginType(UserLoginType.EMAIL).build());
        // then
        mvc.perform(get(BASE_URL + "/mail/exists/testuser@email.com"))
                .andExpect(status().isOk())
                .andDo(document("auth/email_check/fail", responseFields(
                        fieldWithPath("exists").description("이메일 존재 여부, false면 존재하지 않는 것"),
                        fieldWithPath("loginType").description("유저가 회원가입한 경로, 이메일, 구글, 카카오 타입이 존재")
                )));
    }

    @Test
    @DisplayName("닉네임 중복 확인 성공 테스트")
    public void nicknameDuplicateCheckTestToSuccess() throws Exception{
        // when
        when(authService.nicknameExists(any())).thenReturn(MemberDTO.ExistsCheckDto.builder().exists(false).build());
        // then
        mvc.perform(get(BASE_URL + "/nickname/exists/testuser"))
                .andExpect(status().isOk())
                .andDo(document("auth/nickname_check/success", responseFields(
                        fieldWithPath("exists").description("닉네임 존재 여부, false면 존재하지 않는 것"),
                        fieldWithPath("loginType").description("유저가 회원가입한 경로, 이메일, 구글, 카카오 타입이 존재")
                )));
    }

    @Test
    @DisplayName("닉네임 중복 확인 실패 테스트")
    public void nicknameDuplicateCheckTestToFail() throws Exception{
        // when
        when(authService.nicknameExists(any())).thenReturn(MemberDTO.ExistsCheckDto.builder().exists(true).build());
        // then
        mvc.perform(get(BASE_URL + "/nickname/exists/testuser"))
                .andExpect(status().isOk())
                .andDo(document("auth/nickname_check/fail", responseFields(
                        fieldWithPath("exists").description("닉네임 존재 여부, false면 존재하지 않는 것"),
                        fieldWithPath("loginType").description("유저가 회원가입한 경로, 이메일, 구글, 카카오 타입이 존재")
                )));
    }


    @Test
    @DisplayName("회원 가입 성공 테스트")
    public void signUpTestToSuccess() throws Exception {
        // given
        SignUpRequest apiTestUser = createSignUpRequest(EMAIL, NICKNAME);

        // when
        when(authService.signUp(any())).thenReturn(Member.builder()
                .email(EMAIL)
                .nickName(NICKNAME)
                .userName(USERNAME)
                .password(PASSWORD)
                .applicationPassword(APPLICATION_PASSWORD)
                .build());
        when(accountBookService.createAccountBookPrivate(any(), any())).thenReturn(AccountBook.builder()
                .build());
        when(categoryService.createCategories(any(), any())).thenReturn(CategoryCreateResult.builder().build());
        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(apiTestUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/signup/success", requestFields(
                        fieldWithPath("email").description("회원가입할 이메일, 회원가입 요청을 보내기 전에 이메일 인증과 중복 검사가 완료되어야합니다."),
                        fieldWithPath("nickName").description("사용할 멤버의 닉네임, 회원가입 요청을 보내기 전에 중복 검사가 완료되어야합니다."),
                        fieldWithPath("userName").description("회원가입하는 사용자의 본명입니다."),
                        fieldWithPath("password").description("로그인시 사용할 비밀번호로 8자리 이상이어야합니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")


                ), responseFields(
                        fieldWithPath("result").description("회원가입 결과, 회원가입 성공시 CREATED를 return 합니다")
                )));
    }

    @Test
    @DisplayName("8 자리 미만 비밀번호로 회원가입 실패")
    public void signUpTestToFailByShortPassword() throws Exception {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password("1234")
                .nickName(NICKNAME)
                .userName(USERNAME)
                .applicationPassword(APPLICATION_PASSWORD)
                .email(EMAIL)
                .build();


        // then

        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/short_password", requestFields(
                        fieldWithPath("email").description("회원가입할 이메일, 회원가입 요청을 보내기 전에 이메일 인증과 중복 검사가 완료되어야합니다."),
                        fieldWithPath("nickName").description("사용할 멤버의 닉네임, 회원가입 요청을 보내기 전에 중복 검사가 완료되어야합니다."),
                        fieldWithPath("userName").description("회원가입하는 사용자의 본명입니다."),
                        fieldWithPath("password").description("로그인시 사용할 비밀번호로 8자리 이상이어야합니다, 8자리 미만으로 에러가 발생합니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 회원가입 실패")
    public void signUpTestToFailByWrongEmail() throws Exception{
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password(PASSWORD)
                .nickName(NICKNAME)
                .userName(USERNAME)
                .applicationPassword(APPLICATION_PASSWORD)
                .email("email.com")
                .build();
        // then

        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/wrong_email", requestFields(
                        fieldWithPath("email").description("이메일 형식을 지키지 않은 이메일입니다."),
                        fieldWithPath("nickName").description("사용할 멤버의 닉네임, 회원가입 요청을 보내기 전에 중복 검사가 완료되어야합니다."),
                        fieldWithPath("userName").description("회원가입하는 사용자의 본명입니다."),
                        fieldWithPath("password").description("로그인시 사용할 비밀번호로 8자리 이상이어야합니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("null password로 회원가입 실패")
    public void signUpTestToFailByNullPassword() throws Exception{
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .nickName(NICKNAME)
                .userName(USERNAME)
                .applicationPassword(APPLICATION_PASSWORD)
                .email(EMAIL)
                .build();



        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/null_password", requestFields(
                        fieldWithPath("email").description("이메일 형식을 지키지 않은 이메일입니다."),
                        fieldWithPath("nickName").description("사용할 멤버의 닉네임, 회원가입 요청을 보내기 전에 중복 검사가 완료되어야합니다."),
                        fieldWithPath("userName").description("회원가입하는 사용자의 본명입니다."),
                        fieldWithPath("password").description("비밀번호로 null로 주어졌습니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("null nickname으로 회원가입 실패")
    public void signUpTestToFailByNullNickname() throws Exception{
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password(PASSWORD)
                .userName(USERNAME)
                .applicationPassword(APPLICATION_PASSWORD)
                .email(EMAIL)
                .build();



        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/null_nickname", requestFields(
                        fieldWithPath("email").description("이메일 형식을 지키지 않은 이메일입니다."),
                        fieldWithPath("nickName").description("null로 주어진 닉네임입니다."),
                        fieldWithPath("userName").description("회원가입하는 사용자의 본명입니다."),
                        fieldWithPath("password").description("비밀번호는 8자리 이상이어야합니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("null username으로 회원가입 실패")
    public void signUpTestToFailByNullUserName() throws Exception{
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password(PASSWORD)
                .nickName(NICKNAME)
                .applicationPassword(APPLICATION_PASSWORD)
                .email(EMAIL)
                .build();



        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/null_username", requestFields(
                        fieldWithPath("email").description("이메일 형식을 지키지 않은 이메일입니다."),
                        fieldWithPath("nickName").description("중복 검사한 닉네임이 주어져야합니다."),
                        fieldWithPath("userName").description("null로 주어진 사용자 본명입니다."),
                        fieldWithPath("password").description("비밀번호는 8자리 이상이어야합니다."),
                        fieldWithPath("applicationPassword").description("애플리케이션 내부에서 사용할 비밀번호로 4자리입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @DisplayName("null applicationPassword로 회원가입 실패")
    public void signUpTestToFailByNullApplicationPassword() throws Exception{
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .password(PASSWORD)
                .nickName(NICKNAME)
                .userName(USERNAME)
                .email(EMAIL)
                .build();



        // then
        mvc.perform(post(BASE_URL + "/signup")
                        .content(mapper.writeValueAsBytes(signUpRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andDo(document("auth/signup/fail/null_applicationPassword", requestFields(
                        fieldWithPath("email").description("이메일 형식을 지키지 않은 이메일입니다."),
                        fieldWithPath("nickName").description("중복 검사한 닉네임이 주어져야합니다."),
                        fieldWithPath("userName").description("사용자 본명입니다."),
                        fieldWithPath("password").description("비밀번호는 8자리 이상이어야합니다."),
                        fieldWithPath("applicationPassword").description("null로 주어진 사용자 본명입니다.")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 409로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }


    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginTestToSuccess() throws Exception{
        // given
       LoginRequest loginRequest = createLoginRequest(EMAIL);

        // when
        when(authService.login(any(), any(), any())).thenReturn(MemberDTO.LoginResult.builder()
                .memberInfo(MemberInfoNativeQ.builder()
                        .id(1L)
                        .userName(USERNAME)
                        .privateAccountBookId(1L)
                        .nickName(NICKNAME)
                        .email(EMAIL)
                        .build())
                .tokenInfo(TokenDTO.TokenIssueDTO.builder()
                        .accessToken("ACCESS_TOKEN")
                        .grantType("Bearer")
                        .accessTokenExpiresIn(10000000L).build()).build());
        // then
        mvc.perform(post(BASE_URL + "/login")
                        .content(mapper.writeValueAsBytes(loginRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("auth/login/success", requestFields(
                        fieldWithPath("email").description("로그인할 멤버 이메일"),
                        fieldWithPath("password").description("로그인할 멤버 비밀번호")
                ), responseFields(
                        fieldWithPath("memberInfo.id").description("로그인한 멤버 아이디"),
                        fieldWithPath("memberInfo.userName").description("로그인한 멤버 본명"),
                        fieldWithPath("memberInfo.privateAccountBookId").description("로그인한 멤버 개인 가계부 아이디"),
                        fieldWithPath("memberInfo.nickName").description("로그인한 멤버 닉네임"),
                        fieldWithPath("memberInfo.email").description("로그인한 멤버 이메일"),
                        fieldWithPath("tokenInfo.accessToken").description("사용자에게 발급되는 엑세스 토큰"),
                        fieldWithPath("tokenInfo.grantType").description("토큰 종류"),
                        fieldWithPath("tokenInfo.accessTokenExpiresIn").description("토큰 만료 시간")
                )));
    }


}