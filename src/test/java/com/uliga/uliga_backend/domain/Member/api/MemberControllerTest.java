package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.ApplicationPasswordCheck;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.GetMemberInfo;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MatchResult;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
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

import java.util.ArrayList;

import static com.uliga.uliga_backend.domain.Member.dto.MemberDTO.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {
    @MockBean
    private MemberService memberService;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    public static final String BASE_URL = "/member";

    @Test
    @WithMockCustomUser
    @DisplayName("로그인한 멤버 정보 조회 성공 테스트")
    public void getLoginMemberInfoSuccessTest() throws Exception {
        // given
        MemberInfoNativeQ memberInfoNativeQ = new MemberInfoNativeQ(1L, 1L, "default", "username", "nickname", "email@email.com");
        GetMemberInfo memberInfo = GetMemberInfo.builder()
                .memberInfo(memberInfoNativeQ)
                .invitations(new ArrayList<>()).build();


        // when
        doReturn(memberInfo).when(memberService).getCurrentMemberInfo(any(), any());

        // then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(memberInfo));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 애플리케이션 비밀번호 확인 성공 테스트")
    public void checkApplicationPasswordTestToSuccess() throws Exception {
        // given
        ApplicationPasswordCheck passwordCheck = ApplicationPasswordCheck.builder()
                .applicationPassword("1234").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);
        MatchResult matchResult = MatchResult.builder()
                .matches(true).build();
        String result = mapper.writeValueAsString(matchResult);
        doReturn(true).when(memberService).checkApplicationPassword(any(), any());
        // then
        mvc.perform(post(BASE_URL + "/applicationPassword")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 애플리케이션 비밀번호 확인 실패 테스트")
    public void checkApplicationPasswordTestToFail() throws Exception {
        // given
        ApplicationPasswordCheck passwordCheck = ApplicationPasswordCheck.builder()
                .applicationPassword("1234").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);

        MatchResult wrong = MatchResult.builder()
                .matches(false).build();
        String result = mapper.writeValueAsString(wrong);
        doReturn(false).when(memberService).checkApplicationPassword(any(), any());
        // then
        mvc.perform(post(BASE_URL + "/applicationPassword")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }
    // TODO 변경해야된다
    @Test
    @WithMockCustomUser
    @DisplayName("멤버 애플리케이션 비밀번호 업데이트 성공 테스트")
    public void updateApplicationPasswordTestToSuccess() throws Exception {
        // given
        MemberInfoUpdateRequest build = MemberInfoUpdateRequest.builder().applicationPassword("4321").build();

        // when
        String body = mapper.writeValueAsString(build);
        doReturn(build).when(memberService).updateMemberInfo(any(), any());
        // then
        mvc.perform(patch(BASE_URL + "/applicationPassword")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(build);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 비밀번호 확인 성공 테스트")
    public void checkPasswordTestToSuccess() throws Exception {
        // given
        PasswordCheck passwordCheck = PasswordCheck.builder().password("12345678").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);
        MatchResult matchResult = MatchResult.builder()
                .matches(true).build();
        String result = mapper.writeValueAsString(matchResult);
        doReturn(true).when(memberService).checkPassword(any(), any());
        // then
        mvc.perform(post(BASE_URL + "/password")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 비밀번호 확인 실패 테스트")
    public void checkPasswordTestToFail() throws Exception {
        // given
        PasswordCheck passwordCheck = PasswordCheck.builder().password("12345678").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);
        MatchResult matchResult = MatchResult.builder()
                .matches(false).build();
        String result = mapper.writeValueAsString(matchResult);
        doReturn(false).when(memberService).checkPassword(any(), any());
        // then
        mvc.perform(post(BASE_URL + "/password")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }
    // TODO 재작성해야함
    @Test
    @WithMockCustomUser
    @DisplayName("멤버 비밀번호 업데이트 성공 테스트")
    public void updatePasswordTestToSuccess() throws Exception {
        // given
        MemberInfoUpdateRequest build = MemberInfoUpdateRequest.builder().password("123456789").build();

        // when
        String body = mapper.writeValueAsString(build);
        doReturn(build).when(memberService).updateMemberInfo(any(), any());
        // then
        mvc.perform(patch(BASE_URL)
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(body);
    }
    // TODO 재작성해야함
    @Test
    @WithMockCustomUser
    @DisplayName("멤버 프사 변경 성공 테스트")
    public void updateAvatarUrlTestToSuccess() throws Exception {
        // given
        MemberInfoUpdateRequest build = MemberInfoUpdateRequest.builder().avatarUrl("red").build();


        // when
        String body = mapper.writeValueAsString(build);
        doReturn(build).when(memberService).updateMemberInfo(any(), any());
        // then
        mvc.perform(patch(BASE_URL)
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(body);
    }
    // TODO 다시
    @Test
    @WithMockCustomUser
    @DisplayName("멤버 닉네임 변경 성공 테스트")
    public void updateMemberNicknameTestToSuccess() throws Exception {
        // given
        MemberInfoUpdateRequest build = MemberInfoUpdateRequest.builder().nickName("newNickname").build();


        // when
        String body = mapper.writeValueAsString(build);
        doReturn(build).when(memberService).updateMemberInfo(any(), any());
        // then
        mvc.perform(patch(BASE_URL)
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(body);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 닉네임 중복 여부 확인 성공 테스트")
    public void checkMemberNicknameTestToSuccess() throws Exception {
        // given
        NicknameCheckDto build = NicknameCheckDto.builder().nickname("newNickname").build();

        // when
        String body = mapper.writeValueAsString(build);
        doReturn(false).when(memberService).nicknameExists(any(), any());
        ExistsCheckDto checkDto = ExistsCheckDto.builder().exists(false).build();
        String result = mapper.writeValueAsString(checkDto);
        // then
        mvc.perform(post(BASE_URL + "/nickname")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 닉네임 중복 여부 확인 실패 테스트")
    public void checkMemberNicknameTestToFail() throws Exception {
        // given
        NicknameCheckDto build = NicknameCheckDto.builder().nickname("newNickname").build();

        // when
        String body = mapper.writeValueAsString(build);
        doReturn(true).when(memberService).nicknameExists(any(), any());
        ExistsCheckDto checkDto = ExistsCheckDto.builder().exists(true).build();
        String result = mapper.writeValueAsString(checkDto);
        // then
        mvc.perform(post(BASE_URL + "/nickname")
                        .with(csrf())
                        .content(body)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 탈퇴 성공 테스트")
    public void deleteMemberTestToSuccess() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(any());

        // then
        mvc.perform(delete(BASE_URL).with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals("DELETED");
    }
}