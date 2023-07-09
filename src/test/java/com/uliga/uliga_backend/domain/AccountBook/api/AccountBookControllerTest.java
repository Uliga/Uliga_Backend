package com.uliga.uliga_backend.domain.AccountBook.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.application.AccountBookService;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AccountBookInfo;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AccountBookUpdateRequest;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.GetAccountBookInfos;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.application.AccountBookDataService;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.AccountBookDataDailySum;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
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

import java.util.*;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import static com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority.ADMIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(AccountBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AccountBookControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    private AccountBookService accountBookService;
    @MockBean
    private IncomeService incomeService;
    @MockBean
    private RecordService recordService;
    @MockBean
    private BudgetService budgetService;
    @MockBean
    private ScheduleService scheduleService;
    @MockBean
    private AccountBookDataService accountBookDataService;
    @MockBean
    private CategoryService categoryService;
    private final String BASE_URL = "/accountBook";

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(sharedHttpSession())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }

    private final List<AccountBookMemberInfoQ> defaultMembers = new ArrayList<>(
            Arrays.asList(new AccountBookMemberInfoQ(1L, "user2", ADMIN, "default", "test1@user.com"),
                    new AccountBookMemberInfoQ(2L, "user1", ADMIN, "default", "test2@user.com"))
    );
    private final List<AccountBookCategoryInfoQ> defaultCategories = new ArrayList<>(
            Arrays.asList(new AccountBookCategoryInfoQ(1L, "\uD83C\uDF7D️ 식비"),
                    new AccountBookCategoryInfoQ(2L, "☕ 카페 · 간식"),
                    new AccountBookCategoryInfoQ(3L, "\uD83C\uDFE0 생활"),
                    new AccountBookCategoryInfoQ(4L, "\uD83C\uDF59 편의점,마트,잡화"),
                    new AccountBookCategoryInfoQ(5L, "\uD83D\uDC55 쇼핑"),
                    new AccountBookCategoryInfoQ(6L, "기타")
            )
    );


    @Test
    @WithMockCustomUser
    @DisplayName("멤버 가계부 조회 성공 테스트")
    public void getMemberAccountBookTest() throws Exception {
        // given
        AccountBookInfo accountBookInfo = AccountBookInfo.builder().members(defaultMembers).numberOfMember(new MembersQ(2L)).info(new AccountBookInfoQ(1L, false, "테스트용", ADMIN, true, "테스트용", "default")).categories(defaultCategories).build();
        List<AccountBookInfo> accountBookInfoList = new ArrayList<>();
        accountBookInfoList.add(accountBookInfo);
        GetAccountBookInfos getAccountBookInfos = GetAccountBookInfos.builder().accountBooks(accountBookInfoList).build();

        // when
        when(accountBookService.getMemberAccountBook(any())).thenReturn(getAccountBookInfos);

        // then
        mvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/get_member_accountBookInfo/success"));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("단일 가계부 정보 조회 테스트")
    public void getSingleAccountBookInfo() throws Exception {
        // given
        AccountBookInfo accountBookInfo = AccountBookInfo.builder().members(defaultMembers).numberOfMember(new MembersQ(2L)).info(new AccountBookInfoQ(1L, false, "테스트용", ADMIN, true, "테스트용", "default")).categories(defaultCategories).build();


        // when
        when(accountBookService.getSingleAccountBookInfo(any(), any())).thenReturn(accountBookInfo);

        // then
        mvc.perform(get(BASE_URL + "/1")).andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/get_single_accountBookInfo/success"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("속하지 않은 가계부 정보 요청으로 인한 조회 실패")
    public void getSingleAccountBookInfoTestToFail() throws Exception {
        // given
        AccountBookInfo accountBookInfo = AccountBookInfo.builder().members(defaultMembers).numberOfMember(new MembersQ(2L)).info(new AccountBookInfoQ(1L, false, "테스트용", ADMIN, true, "테스트용", "default")).categories(defaultCategories).build();


        // when
        when(accountBookService.getSingleAccountBookInfo(any(), any())).thenThrow(UnauthorizedAccountBookAccessException.class);

        // then
        mvc.perform(get(BASE_URL + "/1")).andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("accountBook/get_single_accountBookInfo/fail/invalid_access", responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 400로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("가계부 정보 업데이트 테스트")
    public void accountBookUpdateTest() throws Exception {
        // given
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("avatarUrl", "변경할 아바타");
        updateRequest.put("relationship", "변경할 별칭");
        updateRequest.put("name", "변경할 이름");
        updateRequest.put("members", new ArrayList<>(
                Arrays.asList("남을 멤버1", "남을 멤버2")
        ));
        updateRequest.put("categories", new ArrayList<>(
                Arrays.asList("남을 카테고리1", "남을 카테고리2")
        ));
        AccountBookUpdateRequest accountBookUpdateRequest = AccountBookUpdateRequest.builder().avatarUrl("변경된 아바타").relationship("변경된 별칭").name("변경된 이름").members(new ArrayList<>(
                Arrays.asList("남을 멤버1", "남을 멤버2")
        )).categories(new ArrayList<>(
                Arrays.asList("남을 카테고리1", "남을 카테고리2")
        )).build();


        // when
        when(accountBookService.updateAccountBookInfo(any(), any(), any())).thenReturn(accountBookUpdateRequest);
        when(categoryService.updateAccountBookCategory(any(), any())).thenReturn("UPDATED");

        // then
        mvc.perform(patch(BASE_URL + "/1").content(mapper.writeValueAsBytes(updateRequest)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/update/success", requestFields(
                        fieldWithPath("avatarUrl").description("변경할 아바타"),
                        fieldWithPath("relationship").description("변경할 별칭"),
                        fieldWithPath("name").description("변경할 이름"),
                        fieldWithPath("members").description("변경할 멤버 리스트"),
                        fieldWithPath("categories").description("변경할 카테고리 리스트")
                ), responseFields(
                        fieldWithPath("avatarUrl").description("변경할 아바타"),
                        fieldWithPath("relationship").description("변경할 별칭"),
                        fieldWithPath("name").description("변경할 이름"),
                        fieldWithPath("members").description("변경할 멤버 리스트"),
                        fieldWithPath("categories").description("변경할 카테고리 리스트")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("속하지 않은 가계부 업데이트 요청 실패")
    public void updateAccountBookFail() throws Exception{
        // given
        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("avatarUrl", "변경할 아바타");
        updateRequest.put("relationship", "변경할 별칭");
        updateRequest.put("name", "변경할 이름");
        updateRequest.put("members", new ArrayList<>(
                Arrays.asList("남을 멤버1", "남을 멤버2")
        ));
        updateRequest.put("categories", new ArrayList<>(
                Arrays.asList("남을 카테고리1", "남을 카테고리2")
        ));


        // when
        when(accountBookService.updateAccountBookInfo(any(), any(), any())).thenThrow(UnauthorizedAccountBookAccessException.class);

        // then
        mvc.perform(patch(BASE_URL + "/1").content(mapper.writeValueAsBytes(updateRequest)).contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("accountBook/update/fail/invalid_access", requestFields(
                        fieldWithPath("avatarUrl").description("변경할 아바타"),
                        fieldWithPath("relationship").description("변경할 별칭"),
                        fieldWithPath("name").description("변경할 이름"),
                        fieldWithPath("members").description("변경할 멤버 리스트"),
                        fieldWithPath("categories").description("변경할 카테고리 리스트")
                ), responseFields(
                        fieldWithPath("errorCode").description("발생한 에러 코드입니다. 이경우에는 400로 리턴됩니다."),
                        fieldWithPath("message").description("발생한 에러에 대한 설명입니다.")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("가계부 생성 성공 테스트")
    public void createAccountBookTest() throws Exception{
        // given
        AccountBookCreateRequest createRequest = AccountBookCreateRequest.builder()
                .name("가계부 이름")
                .relationship("가계부 별칭")
                .categories(new ArrayList<>(Arrays.asList("카테고리1", "카테고리2")))
                .emails(new ArrayList<>(Arrays.asList("초대할 사람1 이메일", "초대할 사람2 이메일"))).build();

        AccountBook accountBook = AccountBook.builder().isPrivate(false).relationShip("가계부 별칭").name("가계부 이름").build();

        // when
        when(accountBookService.createAccountBook(any(), any())).thenReturn(accountBook);
        when(categoryService.createCategoriesForNewAccountBook(any(), any())).thenReturn("CREATED");
        // then
        mvc.perform(post(BASE_URL)
                        .content(mapper.writeValueAsBytes(createRequest))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/create/success", requestFields(
                        fieldWithPath("name").description("생성할 가계부 이름"),
                        fieldWithPath("relationship").description("생성할 가계부 별칭"),
                        fieldWithPath("categories").description("생성할 가계부 카테고리들"),
                        fieldWithPath("emails").description("초대할 멤버 이메일들")
                ), responseFields(
                        fieldWithPath("id").description("생성된 가계부 아이디"),
                        fieldWithPath("isPrivate").description("개인 / 공유 가계부 여부"),
                        fieldWithPath("relationShip").description("가계부 별칭"),
                        fieldWithPath("name").description("생성한 가계부 이름")
                )));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("가계부 멤버 초대 성공 테스트")
    public void inviteMemberSuccessTest() throws Exception{
        // given
        List<String> emails = new ArrayList<>(
                Arrays.asList("user1@email.com", "user2@email.com")
        );
        GetInvitations invitations = GetInvitations.builder().id(1L).emails(emails).build();

        // when
        when(accountBookService.createInvitation(any(), any())).thenReturn(Invited.builder().invited(2L).build());
        // then
        mvc.perform(post(BASE_URL + "/invitation")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(invitations)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/invitation/success", requestFields(
                        fieldWithPath("id").description("초대할 공유 가계부 아이디"),
                        fieldWithPath("emails").description("초대할 사람들 이메일")
                ), responseFields(
                        fieldWithPath("invited").description("초대된 사람들 수")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("가계부 초대 응답 테스트")
    public void invitationReplySuccessTest() throws Exception{
        // given
        InvitationReply invitationReply = InvitationReply.builder().accountBookName("가계부 이름").id(1L).memberName("멤버 이름").join(true).build();


        // when
        when(accountBookService.invitationReply(any(), any())).thenReturn(InvitationReplyResult.builder().id(1L).join(true).build());

        // then
        mvc.perform(post(BASE_URL + "/invitation/reply")
                        .content(mapper.writeValueAsBytes(invitationReply))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/invitation/reply/success", requestFields(
                        fieldWithPath("accountBookName").description("초대에 응답하는 가계부 이름"),
                        fieldWithPath("id").description("초대에 응답하는 가계부 아이디"),
                        fieldWithPath("memberName").description("초대에 응답하는 멤버 이름"),
                        fieldWithPath("join").description("가계부에 들어갈지 여부")
                ), responseFields(
                        fieldWithPath("id").description("초대에 응답한 가계부 아이디"),
                        fieldWithPath("join").description("가계부에 들어갔는지 여부")
                )));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("한달 가계부 날짜별 수입 / 지출 합 조회 성공 테스트")
    public void accountBookDailySumTest() throws Exception{
        // given
        AccountBookDataDailySum accountBookDataDailySum = AccountBookDataDailySum.builder().records(new ArrayList<>()).incomes(new ArrayList<>()).build();

        // when
        when(accountBookDataService.getAccountBookItems(any(), any(), any())).thenReturn(accountBookDataDailySum);

        // then
        mvc.perform(get(BASE_URL + "/1/item/2023/4"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("accountBook/daily_item_sum/success", responseFields(
                        fieldWithPath("records").description("날짜별 지출 합 리스트"),
                        fieldWithPath("incomes").description("날짜별 수입 합 리스트")
                )));
    }
}