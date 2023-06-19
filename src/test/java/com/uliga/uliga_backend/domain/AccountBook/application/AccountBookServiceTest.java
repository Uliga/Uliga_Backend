package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AccountBookCreateRequest;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.GetAccountBookInfos;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.SimpleAccountBookInfo;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
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
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.application.AuthService;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
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
import java.util.Optional;

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

    @Test
    @DisplayName("가계부 단일 정보 조회 실패 테스트 - 멤버 & 가계부 불일치")
    public void getSingleAccountBookInfoFailTest() throws Exception{
        // given

        Long memberId = 1L;
        Long accountBookId = 1L;
        // when
        when(accountBookRepository.findAccountBookInfoById(1L, 1L)).thenReturn(null);

        // then
        assertThrows(UnauthorizedAccountBookAccessException.class, () -> accountBookService.getSingleAccountBookInfo(accountBookId, memberId));
    }

    @Test
    @DisplayName("멤버 가계부 조회 성공 테스트")
    public void getMemberAccountBooksSuccessTest() throws Exception{
        // given

        Long memberId = 1L;
        AccountBookInfoQ accountBookInfo = new AccountBookInfoQ(1L, true, "testAccountBook", AccountBookAuthority.ADMIN, true, "relationship", "default");
        List<AccountBookInfoQ> accountBookInfoQS = new ArrayList<>();
        accountBookInfoQS.add(accountBookInfo);
        MembersQ membersQ = new MembersQ(1L);
        List<AccountBookMemberInfoQ> memberInfoQS = new ArrayList<>();
        List<AccountBookCategoryInfoQ> categoryInfoQS = new ArrayList<>();
        // when
        when(accountBookRepository.findAccountBookInfosByMemberId(1L)).thenReturn(accountBookInfoQS);
        when(accountBookRepository.getMemberNumberByAccountBookId(1L)).thenReturn(membersQ);
        when(accountBookRepository.findAccountBookMemberInfoById(1L)).thenReturn(memberInfoQS);
        when(accountBookRepository.findAccountBookCategoryInfoById(1L)).thenReturn(categoryInfoQS);

        GetAccountBookInfos memberAccountBook = accountBookService.getMemberAccountBook(memberId);
        // then
        assertAll(
                () -> assertEquals(memberAccountBook.getAccountBooks().get(0).getInfo(), accountBookInfo)
        );

    }


    @Test
    @DisplayName("가계부 생성 성공 테스트")
    public void createAccountBookSuccessTest() throws Exception{
        // given
        Optional<Member> member = Optional.of(new Member());
        when(memberRepository.findById(1L)).thenReturn(member);
        when(accountBookRepository.save(any())).thenReturn(new AccountBook());
        when(accountBookMemberRepository.save(any())).thenReturn(new AccountBookMember());
        when(categoryService.createCategories(any(), any())).thenReturn(new ArrayList<>());
        // when
        SimpleAccountBookInfo accountBook = accountBookService.createAccountBook(1L, new AccountBookCreateRequest("가계부 이름", defaultCategories, new ArrayList<>(), "별칭"));
        // then
        assertAll(
                () -> assertEquals(accountBook.getName(), "가계부 이름"),
                () -> assertEquals(accountBook.getRelationShip(), "별칭")
        );
    }

    @Test
    @DisplayName("가계부 생성 실패 테스트 - 유효하지 않은 멤버 아이디")
    public void createAccountBookFailByMemberIdTest() throws Exception{
        // given
        Long memberId = 1L;
        Optional<Member> optional = Optional.empty();

        // when
        when(memberRepository.findById(1L)).thenReturn(optional);

        // then
        assertThrows(NotFoundByIdException.class, () -> accountBookService.createAccountBook(1L, new AccountBookCreateRequest()));

    }
}