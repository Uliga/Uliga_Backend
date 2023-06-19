package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataMapper;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataRepository;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AccountBookInfo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class AccountBookServiceTest {

    private AccountBookService accountBookService;
    @Mock
    private AccountBookRepository accountBookRepository;
    @Mock
    private AccountBookMemberRepository accountBookMemberRepository;
    @Mock
    private AccountBookDataRepository accountBookDataRepository;
    @Mock
    private AccountBookDataMapper accountBookDataMapper;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private IncomeRepository incomeRepository;
    @Mock
    private RecordRepository recordRepository;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private RecordService recordService;
    @Mock
    private IncomeService incomeService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        accountBookService = new AccountBookService(
                accountBookRepository,
                accountBookMemberRepository,
                accountBookDataRepository,
                accountBookDataMapper,
                memberRepository,
                incomeRepository,
                recordRepository,
                budgetRepository,
                redisTemplate,
                objectMapper,
                categoryRepository,
                categoryService,
                recordService,
                incomeService,
                scheduleService,
                budgetService
        );
    }

    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    @Test
    @DisplayName("가계부 단일 정보 조회 성공 테스트")
    public void getSingleAccountBookInfoSuccessTest() throws Exception {
        // given
        // 서비스 메서드 호출시 리턴할 AccountBookInfo
        AccountBookInfoQ accountBookInfo = new AccountBookInfoQ(1L, true, "testAccountBook", AccountBookAuthority.ADMIN, true, "relationship", "default");
        MembersQ membersQ = new MembersQ(1L);
        List<AccountBookMemberInfoQ> memberInfoQS = new ArrayList<>();
        List<AccountBookCategoryInfoQ> categoryInfoQS = new ArrayList<>();

        Long memberId = 1L;
        Long accountBookId = 1L;


        when(accountBookRepository.findAccountBookInfoById(1L, 1L)).thenReturn(accountBookInfo);
        when(accountBookRepository.getMemberNumberByAccountBookId(1L)).thenReturn(membersQ);
        when(accountBookRepository.findAccountBookMemberInfoById(1L)).thenReturn(memberInfoQS);
        when(accountBookRepository.findAccountBookCategoryInfoById(1L)).thenReturn(categoryInfoQS);
        // when
        AccountBookInfo singleAccountBookInfo = accountBookService.getSingleAccountBookInfo(accountBookId, memberId);


        // then
        assertAll(
                () -> assertEquals(singleAccountBookInfo.getInfo(), accountBookInfo),
                () -> assertEquals(singleAccountBookInfo.getNumberOfMember(), membersQ),
                () -> assertEquals(singleAccountBookInfo.getMembers(), memberInfoQS),
                () -> assertEquals(singleAccountBookInfo.getCategories(), categoryInfoQS)
        );

    }


}