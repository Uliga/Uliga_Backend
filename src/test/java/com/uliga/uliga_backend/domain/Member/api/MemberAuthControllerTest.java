package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.CategoryCreateResult;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.application.EmailCertificationService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.CodeConfirmDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.ConfirmEmailDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.EmailConfirmCodeDto;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SignUpRequest;
import com.uliga.uliga_backend.domain.Member.exception.CannotLoginException;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Member.model.UserLoginType;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.LoginRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    @DisplayName("회원 가입 성공 테스트")
    public void signupTestToSuccess() throws Exception {
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

}