package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import com.uliga.uliga_backend.domain.AccountBook.exception.InvalidAccountBookDeleteRequest;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
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

import java.util.*;

import static org.mockito.Mockito.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 생성 성공 테스트")
//     void createAccountBookTestToSuccess() throws Exception {
//        // given
//        AccountBookCreateRequest accountBook = createAccountBook("가계부 생성");
//        String accountBookCreateRequest = mapper.writeValueAsString(accountBook);
//        SimpleAccountBookInfo simpleAccountBookInfo = SimpleAccountBookInfo.builder().build();
//        // when
//        doReturn(simpleAccountBookInfo).when(accountBookService).createAccountBook(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL)
//                        .with(csrf())
//                        .content(accountBookCreateRequest)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(simpleAccountBookInfo));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null name - 가계부 생성 실패 테스트")
//     void createAccountBookTestToFailByNullName() throws Exception {
//        // given
//        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().emails(new ArrayList<>()).relationship("relationship").categories(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(createRequest);
//
//        // then
//        mvc.perform(post(BASE_URL)
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null emails - 가계부 생성 실패 테스트")
//     void createAccountBookTestToFailByNullEmails() throws Exception {
//        // given
//        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().name("name").categories(new ArrayList<>()).relationship("relationship").build();
//        String value = mapper.writeValueAsString(createRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL)
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null categories - 가계부 생성 실패 테스트")
//     void createAccountBookTestToFailByNullCategories() throws Exception {
//        // given
//        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder().name("name").emails(new ArrayList<>()).relationship("relationship").build();
//        String value = mapper.writeValueAsString(createRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL)
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("멤버 가계부 조회 성공 테스트")
//     void getMemberAccountBookTestToSuccess() throws Exception {
//        // given
//        GetAccountBookInfos accountBookInfos = GetAccountBookInfos.builder().accountBooks(new ArrayList<>()).build();
//
//        // when
//        doReturn(accountBookInfos).when(accountBookService).getMemberAccountBook(any());
//
//        // then
//        mvc.perform(get(BASE_URL)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookInfos));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("아이디로 가계부 정보조회 성공 테스트")
//     void getAccountBookInfoTestToSuccess() throws Exception {
//        // given
//        AccountBookInfo accountBookInfo = AccountBookInfo.builder().build();
//
//        // when
//        doReturn(accountBookInfo).when(accountBookService).getSingleAccountBookInfo(any(), any());
//
//        // then
//        mvc.perform(get(BASE_URL + "/1")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookInfo));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("멤버 초대 성공 테스트")
//     void inviteMemberTestToSuccess() throws Exception {
//        // given
//        Invited invited = Invited.builder().invited(0L).build();
//        GetInvitations invitations = GetInvitations.builder().id(1L).emails(new ArrayList<>()).build();
//        String invitation = mapper.writeValueAsString(invitations);
//
//        // when
//        doReturn(invited).when(accountBookService).createInvitation(any(), any());
//        // then
//        mvc.perform(post(BASE_URL + "/invitation")
//                        .with(csrf())
//                        .content(invitation)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(invited));
//
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("초대 응답 성공 테스트")
//     void invitationReplyTestToSuccess() throws Exception {
//        // given
//        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").join(true).id(1L).build();
//        InvitationReplyResult invitationReplyResult = InvitationReplyResult.builder().build();
//        String value = mapper.writeValueAsString(invitationReply);
//
//
//        // when
//        doReturn(invitationReplyResult).when(accountBookService).invitationReply(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/invitation/reply")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(invitationReplyResult));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 초대 응답 실패 테스트")
//     void invitationReplyTestToFailByNullId() throws Exception {
//        // given
//        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").join(true).build();
//        // when
//        String value = mapper.writeValueAsString(invitationReply);
//
//        // then
//        mvc.perform(post(BASE_URL + "/invitation/reply")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null membername - 초대 응답 실패 테스트")
//     void invitationReplyTestToFailByNullMemberName() throws Exception {
//        // given
//        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").join(true).id(1L).build();
//        // when
//        String value = mapper.writeValueAsString(invitationReply);
//
//        // then
//        mvc.perform(post(BASE_URL + "/invitation/reply")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null accountBookName - 초대 응답 실패 테스트")
//     void invitationReplyTestToFailByNullAccountBookName() throws Exception {
//        // given
//        InvitationReply invitationReply = InvitationReply.builder().memberName("member").join(true).id(1L).build();
//        // when
//        String value = mapper.writeValueAsString(invitationReply);
//
//        // then
//        mvc.perform(post(BASE_URL + "/invitation/reply")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null join - 초대 응답 실패 테스트")
//     void invitationReplyTestToFailByNullJoin() throws Exception {
//        // given
//        InvitationReply invitationReply = InvitationReply.builder().accountBookName("accountBook").memberName("member").id(1L).build();
//        // when
//        String value = mapper.writeValueAsString(invitationReply);
//
//        // then
//        mvc.perform(post(BASE_URL + "/invitation/reply")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("한달 가계부 수입/지출 정보 조회 성공 테스트")
//     void getMonthlyIncomeRecordTestToSuccess() throws Exception {
//        // given
//        AccountBookIncomesAndRecords accountBookIncomesAndRecords = AccountBookIncomesAndRecords.builder().build();
//
//        // when
//        doReturn(accountBookIncomesAndRecords).when(accountBookService).getAccountBookItems(any(), any(), any());
//
//
//        // then
//        mvc.perform(get(BASE_URL + "/1/item/2023/3")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookIncomesAndRecords));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("하루 수입/지출 내역 상세 조회 성공 테스트")
//     void getDailyIncomeAndRecordInfoTestToSuccess() throws Exception {
//        // given
//        RecordAndIncomeDetails recordAndIncomeDetails = RecordAndIncomeDetails.builder().build();
//
//        // when
//        doReturn(recordAndIncomeDetails).when(accountBookService).getAccountBookItemDetails(any(), any(), any(), any());
//
//        // then
//        mvc.perform(get(BASE_URL + "/1/item/2023/3/1")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(recordAndIncomeDetails));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("한달 수입/지출/예산 내역 총합 조회 성공 테스트")
//     void getMonthlyIncomeRecordBudgetSumTestToSuccess() throws Exception {
//        // given
//        GetAccountBookAssets accountBookAssets = GetAccountBookAssets.builder().build();
//        // when
//        doReturn(accountBookAssets).when(accountBookService).getAccountBookAssets(any(), any(), any());
//
//        // then
//        mvc.perform(get(BASE_URL + "/1/asset/2023/3")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(accountBookAssets));
//
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 수입/지출 추가 성공 테스트")
//     void createItemsTestToSuccess() throws Exception {
//        // given
//
//        CreateItems createItems = CreateItems.builder().id(0L).createRequest(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(createItems);
//        CreateResult result = CreateResult.builder().created(new ArrayList<>()).record(0L).income(0L).build();
//
//        // when
//        doReturn(result).when(accountBookService).createItems(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/item")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 가계부 수입/지출 추가 실패 테스트")
//     void createItemsTestToFailByNullId() throws Exception {
//        // given
//        CreateItems createItems = CreateItems.builder().createRequest(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(createItems);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/item")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null createRequest - 가계부 수입/지출 추가 실패 테스트")
//     void createItemsTestToFailByNullCreateRequest() throws Exception {
//        // given
//        CreateItems createItems = CreateItems.builder().createRequest(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(createItems);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/item")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 카테고리 추가 성공 테스트")
//     void createCategoriesTestToSuccess() throws Exception {
//        // given
//        CategoryCreateRequest createRequest = CategoryCreateRequest.builder().categories(new ArrayList<>()).id(0L).build();
//        String value = mapper.writeValueAsString(createRequest);
//        CategoryCreateResult result = CategoryCreateResult.builder().created(new ArrayList<>()).id(0L).build();
//        // when
//        doReturn(result).when(accountBookService).createCategory(any(), any());
//        // then
//        mvc.perform(post(BASE_URL + "/category")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 가계부 카테고리 추가 실패 테스트")
//     void createCategoriesTestToFailByNullId() throws Exception {
//        // given
//        CategoryCreateRequest createRequest = CategoryCreateRequest.builder().categories(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(createRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/category")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null categories - 가계부 카테고리 추가 실패 테스트")
//     void createCategoriesTestToFailByNullCategories() throws Exception {
//        // given
//        CategoryCreateRequest createRequest = CategoryCreateRequest.builder().id(0L).build();
//        String value = mapper.writeValueAsString(createRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/category")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부에 지출 한개 추가 성공 테스트")
//     void addRecordTestToSuccess() throws Exception {
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//        AddRecordResult result = AddRecordResult.builder().build();
//
//        // when
//        doReturn(result).when(accountBookService).addRecord(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullId() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null account - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullAccount() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null sharedAccountBook - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullSharedAccountBook() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null payment - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullPayment() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null date - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullDate() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null category - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullCategory() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null value - 가계부에 지출 추가 실패 테스트")
//     void addRecordTestToFailByNullValue() throws Exception{
//        // given
//        AddRecordRequest recordRequest = AddRecordRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .build();
//        String value = mapper.writeValueAsString(recordRequest);
//
//
//        // then
//        mvc.perform(post(BASE_URL + "/record")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부에 수입 추가 성공 테스트")
//     void addIncomeTestToSuccess() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//        AddIncomeResult result = AddIncomeResult.builder().build();
//
//        // when
//        doReturn(result).when(accountBookService).addIncome(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null account - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullAccount() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null sharedAccountBook - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullSharedAccountBook() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null payment - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullPayment() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null date - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullDate() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .category("category")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null category - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullCategory() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .id(0L)
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullId() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .value(1000L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null value - 가계부 수입 추가 실패 테스트")
//     void addIncomeTestToFailByNullValue() throws Exception{
//        // given
//        AddIncomeRequest incomeRequest = AddIncomeRequest.builder()
//                .account("account")
//                .sharedAccountBook(new ArrayList<>())
//                .memo("memo")
//                .payment("payment")
//                .date("2023-03-23")
//                .category("category")
//                .id(0L)
//                .build();
//        String value = mapper.writeValueAsString(incomeRequest);
//
//        // then
//        mvc.perform(post(BASE_URL + "/income")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 카테고리 조회 성공 테스트")
//     void getAccountBookCategoryTestToSuccess() throws Exception{
//        // given
//        AccountBookCategories result = AccountBookCategories.builder().categories(new ArrayList<>()).build();
//
//
//        // when
//        doReturn(result).when(accountBookService).getAccountBookCategories(any());
//
//        // then
//        mvc.perform(get(BASE_URL + "/1/category")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 멤버 조회 성공 테스트")
//     void getAccountBookMemberTestToSuccess() throws Exception{
//        // given
//        AccountBookMembers result = AccountBookMembers.builder().members(new ArrayList<>()).build();
//
//        // when
//        doReturn(result).when(accountBookService).getAccountBookMembers(any());
//        // then
//        mvc.perform(get(BASE_URL + "/1/member")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 예산 추가 성공 테스트")
//     void addBudgetToAccountBookTestToSuccess() throws Exception{
//        // given
//        Map<String, Object> map = new HashMap<>();
//        String value = mapper.writeValueAsString(map);
//        BudgetInfoQ result = BudgetInfoQ.builder().build();
//
//        // when
//        doReturn(result).when(accountBookService).addBudget(any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/budget")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부에 금융일정 추가 성공 테스트")
//     void addScheduleToAccountBookTestToSuccess() throws Exception{
//        // given
//        AddSchedules schedules = AddSchedules.builder().schedules(new ArrayList<>()).id(0L).build();
//        String value = mapper.writeValueAsString(schedules);
//        AddScheduleResult result = AddScheduleResult.builder().build();
//
//        // when
//        doReturn(result).when(accountBookService).addSchedule(any(), any());
//
//        // then
//        mvc.perform(post(BASE_URL + "/schedule")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null id - 가계부에 금융 일정 추가 실패 테스트")
//     void addScheduleToAccountBookTestToFailByNullId() throws Exception{
//        // given
//        AddSchedules schedules = AddSchedules.builder().schedules(new ArrayList<>()).build();
//        String value = mapper.writeValueAsString(schedules);
//
//        // then
//        mvc.perform(post(BASE_URL + "/schedule")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Null schedules - 가계부에 금융 일정 추가 실패 테스트")
//     void addScheduleToAccountBookTestToFailByNullSchedules() throws Exception{
//        // given
//        AddSchedules schedules = AddSchedules.builder().id(1L).build();
//        String value = mapper.writeValueAsString(schedules);
//
//        // then
//        mvc.perform(post(BASE_URL + "/schedule")
//                        .with(csrf())
//                        .content(value)
//                        .contentType(APPLICATION_JSON))
//                .andExpect(status().isConflict());
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("가계부 삭제 성공 테스트")
//     void deleteAccountBookTestToSuccess() throws Exception{
//        // given
//        AccountBookDeleteRequest deleteRequest = AccountBookDeleteRequest.builder().accountBookId(1L).build();
//        String value = mapper.writeValueAsString(deleteRequest);
//
//
//        // when
//        doReturn("DELETE").when(accountBookService).deleteAccountBook(any(), any());
//        // then
//        mvc.perform(delete(BASE_URL+"/1")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals("DELETE");
//    }
//
//
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("Invalid delete request - 가계부 삭제 실패 테스트")
//     void deleteAccountBookTestToFailByInvalidRequest() throws Exception{
//        // given
//        AccountBookDeleteRequest deleteRequest = AccountBookDeleteRequest.builder().accountBookId(1L).build();
//        String value = mapper.writeValueAsString(deleteRequest);
//
//        // when
//        doThrow(InvalidAccountBookDeleteRequest.class).when(accountBookService).deleteAccountBook(any(), any());
//        // then
//        mvc.perform(delete(BASE_URL+"/1")
//                        .with(csrf()))
//                .andExpect(status().isConflict());
//    }
}