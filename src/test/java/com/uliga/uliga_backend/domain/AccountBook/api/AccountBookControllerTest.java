package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AccountBookControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    AccountBookService accountBookService;
    @MockBean
    IncomeService incomeService;
    @MockBean
    RecordService recordService;
    @MockBean
    BudgetService budgetService;
    @MockBean
    ScheduleService scheduleService;
    private final String BASE_URL = "/accountBook";
    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );


    AccountBookCreateRequest createAccountBook(String name) {
        return AccountBookCreateRequest.builder()
                .name(name)
                .emails(new ArrayList<>())
                .relationship("테스트용")
                .categories(defaultCategories).build();
    }


    @Test
    @WithMockCustomUser
    @DisplayName("가계부 생성 성공 테스트")
    public void createAccountBookTestToSuccess() throws Exception{
        //given
        AccountBookCreateRequest accountBook = createAccountBook("가계부 생성");
        String accountBookCreateRequest = mapper.writeValueAsString(accountBook);
        SimpleAccountBookInfo simpleAccountBookInfo = SimpleAccountBookInfo.builder().build();
        // when
        doReturn(simpleAccountBookInfo).when(accountBookService).createAccountBook(any(), any());

        // then
        mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .content(accountBookCreateRequest)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(simpleAccountBookInfo));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null name - 가계부 생성 실패 테스트")
    public void createAccountBookTestToFailByNullName() throws Exception{
        //given
        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().emails(new ArrayList<>()).relationship("relationship").categories(new ArrayList<>()).build();
        String value = mapper.writeValueAsString(createRequest);

        // then
        mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null emails - 가계부 생성 실패 테스트")
    public void createAccountBookTestToFailByNullEmails() throws Exception{
        //given
        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().name("name").categories(new ArrayList<>()).relationship("relationship").build();
        String value = mapper.writeValueAsString(createRequest);


        // then
        mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null categories - 가계부 생성 실패 테스트")
    public void createAccountBookTestToFailByNullCategories() throws Exception{
        //given
        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().name("name").emails(new ArrayList<>()).relationship("relationship").build();
        String value = mapper.writeValueAsString(createRequest);


        // then
        mvc.perform(post(BASE_URL)
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }



    @Test
    @WithMockCustomUser
    @DisplayName("멤버 가계부 조회 성공 테스트")
    public void getMemberAccountBookTestToSuccess() throws Exception{
        //given
        GetAccountBookInfos accountBookInfos = GetAccountBookInfos.builder().accountBooks(new ArrayList<>()).build();

        // when
        doReturn(accountBookInfos).when(accountBookService).getMemberAccountBook(any());

        // then
        mvc.perform(get(BASE_URL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookInfos));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("아이디로 가계부 정보조회 성공 테스트")
    public void getAccountBookInfoTestToSuccess() throws Exception{
        //given
        AccountBookInfo accountBookInfo = AccountBookInfo.builder().build();

        // when
        doReturn(accountBookInfo).when(accountBookService).getSingleAccountBookInfo(any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookInfo));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 초대 성공 테스트")
    public void inviteMemberTestToSuccess() throws Exception{
        //given
        Invited invited = Invited.builder().invited(0L).build();
        GetInvitations invitations = GetInvitations.builder().id(1L).emails(new ArrayList<>()).build();
        String invitation = mapper.writeValueAsString(invitations);

        // when
        doReturn(invited).when(accountBookService).createInvitation(any(), any());
        // then
        mvc.perform(post(BASE_URL + "/invitation")
                        .with(csrf())
                        .content(invitation)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(invited));

    }
    // TODO 생성 실패 테스트해야함
    @Test
    @WithMockCustomUser
    @DisplayName("초대 응답 성공 테스트")
    public void invitationReplyTestToSuccess() throws Exception{
        //given
        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").join(true).id(1L).build();
        InvitationReplyResult invitationReplyResult = InvitationReplyResult.builder().build();
        String value = mapper.writeValueAsString(invitationReply);


        // when
        doReturn(invitationReplyResult).when(accountBookService).invitationReply(any(), any());

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(invitationReplyResult));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null id - 초대 응답 실패 테스트")
    public void invitationReplyTestToFailByNullId() throws Exception{
        //given
        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").join(true).build();
        // when
        String value = mapper.writeValueAsString(invitationReply);

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null membername - 초대 응답 실패 테스트")
    public void invitationReplyTestToFailByNullMemberName() throws Exception{
        //given
        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").join(true).id(1L).build();
        // when
        String value = mapper.writeValueAsString(invitationReply);

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null accountBookName - 초대 응답 실패 테스트")
    public void invitationReplyTestToFailByNullAccountBookName() throws Exception{
        //given
        InvitationReply invitationReply = InvitationReply.builder().memberName("member").join(true).id(1L).build();
        // when
        String value = mapper.writeValueAsString(invitationReply);

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Null join - 초대 응답 실패 테스트")
    public void invitationReplyTestToFailByNullJoin() throws Exception{
        //given
        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").id(1L).build();
        // when
        String value = mapper.writeValueAsString(invitationReply);

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .with(csrf())
                        .content(value)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockCustomUser
    @DisplayName("한달 가계부 수입/지출 정보 조회 성공 테스트")
    public void getMonthlyIncomeRecordTestToSuccess() throws Exception{
        //given
        AccountBookIncomesAndRecords accountBookIncomesAndRecords = AccountBookIncomesAndRecords.builder().build();

        // when
        doReturn(accountBookIncomesAndRecords).when(accountBookService).getAccountBookItems(any(), any(), any());



        // then
        mvc.perform(get(BASE_URL + "/1/item/2023/3")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookIncomesAndRecords));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("하루 수입/지출 내역 상세 조회 성공 테스트")
    public void getDailyIncomeAndRecordInfoTestToSuccess() throws Exception{
        //given
        RecordAndIncomeDetails recordAndIncomeDetails = RecordAndIncomeDetails.builder().build();

        // when
        doReturn(recordAndIncomeDetails).when(accountBookService).getAccountBookItemDetails(any(), any(), any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1/item/2023/3/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(recordAndIncomeDetails));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("한달 수입/지출/예산 내역 총합 조회 성공 테스트")
    public void getMonthlyIncomeRecordBudgetSumTestToSuccess() throws Exception{
        //given
        GetAccountBookAssets accountBookAssets = GetAccountBookAssets.builder().build();
        // when
        doReturn(accountBookAssets).when(accountBookService).getAccountBookAssets(any(), any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1/asset/2023/3")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookAssets));

    }
}