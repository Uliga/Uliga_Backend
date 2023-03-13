package com.uliga.uliga_backend.domain.Member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.UligaBackendApplication;
import com.uliga.uliga_backend.domain.Member.application.MemberService;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.ApplicationPasswordCheck;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.GetMemberInfo;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.MatchResult;
import com.uliga.uliga_backend.domain.Member.dto.NativeQ.MemberInfoNativeQ;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
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
class MemberControllerTest {
    @Mock
    private MemberService memberService;
    @InjectMocks
    private MemberRepository memberRepository;

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;

    public static final String BASE_URL = "/member";

    @Test
    @WithMockCustomUser
    @DisplayName("로그인한 멤버 정보 조회 성공 테스트")
    public void getLoginMemberInfoSuccessTest() throws Exception{
        //given
        MemberInfoNativeQ memberInfoNativeQ = new MemberInfoNativeQ(1L, 1L, "default", "username", "nickname", "email@email.com");
        GetMemberInfo memberInfo = GetMemberInfo.builder()
                .memberInfo(memberInfoNativeQ)
                .invitations(new ArrayList<>()).build();


        // when
        doReturn(memberInfo).when(memberService).getCurrentMemberInfo(any());

        // then
        mvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(memberInfo));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 애플리케이션 비밀번호 확인 성공 테스트")
    public void checkApplicationPasswordTestToSuccess() throws Exception{
        //given
        ApplicationPasswordCheck passwordCheck = ApplicationPasswordCheck.builder()
                .applicationPassword("1234").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);
        MatchResult matchResult = MatchResult.builder()
                .matches(true).build();
        String result = mapper.writeValueAsString(matchResult);
        doReturn(matchResult).when(memberService).checkApplicationPassword(any(), passwordCheck);
        // then
        mvc.perform(post(BASE_URL + "/applicationPassword")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 애플리케이션 비밀번호 확인 실패 테스트")
    public void checkApplicationPasswordTestToFail() throws Exception{
        //given
        ApplicationPasswordCheck passwordCheck = ApplicationPasswordCheck.builder()
                .applicationPassword("1234").build();

        // when
        String body = mapper.writeValueAsString(passwordCheck);

        MatchResult wrong = MatchResult.builder()
                .matches(false).build();
        String result = mapper.writeValueAsString(wrong);
        doReturn(wrong).when(memberService).checkApplicationPassword(any(), passwordCheck);
        // then
        mvc.perform(post(BASE_URL + "/applicationPassword")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(result);
    }
}