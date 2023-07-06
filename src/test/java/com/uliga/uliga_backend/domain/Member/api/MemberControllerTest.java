package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.GetMemberInfo;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MemberInfoUpdateRequest;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SearchEmailResult;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.SearchMemberByEmail;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    static final String BASE_URL = "/member";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    @Test
    @WithMockCustomUser
    @DisplayName("로그인한 멤버 정보 조회 성공 테스트")
    public void loginMemberTestSuccess() throws Exception {
        // given
        GetMemberInfo memberInfo = GetMemberInfo.builder()
                .memberInfo(MemberInfoNativeQ.builder()
                        .id(1L)
                        .userName("userName")
                        .nickName("nickName")
                        .privateAccountBookId(1L)
                        .email("test_user@email.com")
                        .build())
                .invitations(new ArrayList<>())
                .notifications(new ArrayList<>()).build();


        // when
        when(memberService.getCurrentMemberInfo(any(), any())).thenReturn(memberInfo);

        // then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/get_current_member/success", responseFields(
                        fieldWithPath("memberInfo.id").description("로그인한 멤버 아이디입니다."),
                        fieldWithPath("memberInfo.userName").description("로그인한 멤버 본명입니다."),
                        fieldWithPath("memberInfo.nickName").description("로그인한 멤버 닉네임입니다."),
                        fieldWithPath("memberInfo.email").description("로그인한 멤버 이메일입니다."),
                        fieldWithPath("memberInfo.privateAccountBookId").description("로그인한 멤버 개인 가계부 아이디입니다."),
                        fieldWithPath("invitations").description("새로운 공유 가계부 초대 요청 알림 리스트입니다."),
                        fieldWithPath("notifications").description("할당 받은 금융 일정 알림 리스트입니다.")
                )));
    }

    @Test
    @DisplayName("토큰 정보 없어서 로그인한 멤버 정보 조회 실패 테스트")
    public void loginMemberTestToFail() throws Exception {
        GetMemberInfo memberInfo = GetMemberInfo.builder()
                .memberInfo(MemberInfoNativeQ.builder()
                        .id(1L)
                        .userName("userName")
                        .nickName("nickName")
                        .privateAccountBookId(1L)
                        .email("test_user@email.com")
                        .build())
                .invitations(new ArrayList<>())
                .notifications(new ArrayList<>()).build();


        // when
        when(memberService.getCurrentMemberInfo(any(), any())).thenReturn(memberInfo);

        // then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("member/get_current_member/fail/no_token", responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 401로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 정보 업데이트 성공 테스트")
    public void memberInfoUpdateTest() throws Exception {
        // given
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("applicationPassword", "newApplicationPassword");
        updateRequest.put("password", "newPassword");
        updateRequest.put("nickName", "newNickname");
        updateRequest.put("avatarUrl", "newAvatarUrl");
        MemberInfoUpdateRequest memberInfoUpdateRequest = MemberInfoUpdateRequest.builder()
                .applicationPassword("newApplicationPassword")
                .password("newPassword")
                .nickName("nickName")
                .avatarUrl("avatarUrl")
                .build();

        // when
        when(memberService.updateMemberInfo(any(), any())).thenReturn(memberInfoUpdateRequest);

        // then
        mvc.perform(patch(BASE_URL)
                        .content(mapper.writeValueAsBytes(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/update/success", requestFields(
                        fieldWithPath("applicationPassword").description("업데이트할 애플리케이션 비밀번호"),
                        fieldWithPath("password").description("업데이트할 비밀번호"),
                        fieldWithPath("nickName").description("업데이트할 닉네임"),
                        fieldWithPath("avatarUrl").description("업데이트할 아바타 정보")
                ), responseFields(
                        fieldWithPath("applicationPassword").description("업데이트된 애플리케이션 비밀번호"),
                        fieldWithPath("password").description("업데이트된 비밀번호"),
                        fieldWithPath("nickName").description("업데이트된 닉네임"),
                        fieldWithPath("avatarUrl").description("업데이트된 아바타 정보")
                )));
    }

    @Test
    @DisplayName("토큰 정보 없어서 멤버 업데이트 실패")
    public void updateMemberInfoTestToFailNoToken() throws Exception{
        // given
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("applicationPassword", "newApplicationPassword");
        updateRequest.put("password", "newPassword");
        updateRequest.put("nickName", "newNickname");
        updateRequest.put("avatarUrl", "newAvatarUrl");
        MemberInfoUpdateRequest memberInfoUpdateRequest = MemberInfoUpdateRequest.builder()
                .applicationPassword("newApplicationPassword")
                .password("newPassword")
                .nickName("nickName")
                .avatarUrl("avatarUrl")
                .build();

        // when
        when(memberService.updateMemberInfo(any(), any())).thenReturn(memberInfoUpdateRequest);

        // then
        mvc.perform(patch(BASE_URL)
                        .content(mapper.writeValueAsBytes(updateRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("member/update/fail/no_token", requestFields(
                        fieldWithPath("applicationPassword").description("업데이트할 애플리케이션 비밀번호"),
                        fieldWithPath("password").description("업데이트할 비밀번호"),
                        fieldWithPath("nickName").description("업데이트할 닉네임"),
                        fieldWithPath("avatarUrl").description("업데이트할 아바타 정보")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 401로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("이메일로 존재하는 멤버 찾기")
    public void searchMemberWithEmail() throws Exception{
        // given
        SearchMemberByEmail searchMemberByEmail = SearchMemberByEmail.builder().email("testuser@email.com").build();
        SearchEmailResult searchEmailResult = SearchEmailResult.builder().id(1L).userName("username").nickName("nickname").build();
        // when
        when(memberService.findMemberByEmail(any(), any())).thenReturn(searchEmailResult);
        // then
        mvc.perform(post(BASE_URL + "/search/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(searchMemberByEmail)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/find_by_email/success", requestFields(
                        fieldWithPath("email").description("검색할 이메일")
                ), responseFields(
                        fieldWithPath("id").description("찾은 멤버 아이디"),
                        fieldWithPath("userName").description("찾은 멤버 본명"),
                        fieldWithPath("nickName").description("찾은 멤버 닉네임")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("이메일로 존재하는 멤버 찾기, 멤버 존재 X")
    public void searchMemberWithEmailNoUser() throws Exception{
        // given
        SearchMemberByEmail searchMemberByEmail = SearchMemberByEmail.builder().email("testuser@email.com").build();
        SearchEmailResult searchEmailResult = SearchEmailResult.builder().build();
        // when
        when(memberService.findMemberByEmail(any(), any())).thenReturn(searchEmailResult);
        // then
        mvc.perform(post(BASE_URL + "/search/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(searchMemberByEmail)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/find_by_email/fail/no_user", requestFields(
                        fieldWithPath("email").description("검색할 이메일")
                ), responseFields(
                        fieldWithPath("id").description("존재하지 않는 멤버라 null 리턴"),
                        fieldWithPath("userName").description("존재하지 않는 멤버라 null 리턴"),
                        fieldWithPath("nickName").description("존재하지 않는 멤버라 null 리턴")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("이미 존재하는 멤버라 초대 불가")
    public void searchMemberWithEmailAlreadyJoined() throws Exception{
        // given
        SearchMemberByEmail searchMemberByEmail = SearchMemberByEmail.builder().email("testuser@email.com").build();
        SearchEmailResult searchEmailResult = SearchEmailResult.builder()
                .id(null)
                .userName("이미 존재하는 유저입니다")
                .nickName("이미 존재하는 유저입니다").build();
        // when
        when(memberService.findMemberByEmail(any(), any())).thenReturn(searchEmailResult);
        // then
        mvc.perform(post(BASE_URL + "/search/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(searchMemberByEmail)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member/find_by_email/fail/already_joined", requestFields(
                        fieldWithPath("email").description("검색할 이메일")
                ), responseFields(
                        fieldWithPath("id").description(" null 리턴"),
                        fieldWithPath("userName").description("이미 존재하는 멤버라는 메시지"),
                        fieldWithPath("nickName").description("이미 존재하는 멤버라는 메시지")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("금융 일정 알림 삭제")
    public void memberDeleteNotification() throws Exception{
        // given

        // when

        when(memberService.deleteMemberNotification(any())).thenReturn("DELETED");

        // then
        mvc.perform(delete(BASE_URL + "/notification"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member/notification_remove"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 탈퇴")
    public void memberExitTest() throws Exception{
        // given

        // when

        when(memberService.deleteMember(any())).thenReturn("DELETED");

        // then
        mvc.perform(delete(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member/exit"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("공유 가계부 나가기")
    public void exitFromAccountBook() throws Exception{
        // given


        // when
        when(memberService.deleteAccountBookMember(any(), any())).thenReturn("DELETED");
        // then
        mvc.perform(delete(BASE_URL + "/accountBook/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member/exit_accountBook"));
    }
}