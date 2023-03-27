package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.InvitationInfo;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;
import static com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.CreateBudgetDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final MemberRepository memberRepository;
    private final IncomeRepository incomeRepository;
    private final RecordRepository recordRepository;
    private final BudgetRepository budgetRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;
    private final RecordService recordService;
    private final IncomeService incomeService;
    private final ScheduleService scheduleService;
    private final BudgetService budgetService;
    private final List<String> defaultCategories = new ArrayList<>(
            Arrays.asList("\uD83C\uDF7D️ 식비",
                    "☕ 카페 · 간식",
                    "\uD83C\uDFE0 생활",
                    "\uD83C\uDF59 편의점,마트,잡화",
                    "\uD83D\uDC55 쇼핑",
                    "기타")
    );

    @Transactional
    public AccountBookInfo getSingleAccountBookInfo(Long id, Long memberId) {
        AccountBookInfoQ bookInfoById = accountBookRepository.findAccountBookInfoById(id, memberId);
        if (bookInfoById == null) {
            throw new UnauthorizedAccountBookAccessException();
        }
        return AccountBookInfo.builder()
                .info(bookInfoById)
                .numberOfMember(accountBookRepository.getMemberNumberByAccountBookId(id))
                .members(accountBookRepository.findAccountBookMemberInfoById(id))
                .categories(accountBookRepository.findAccountBookCategoryInfoById(id)).build();
    }

    @Transactional
    public GetAccountBookInfos getMemberAccountBook(Long id) {
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);

        return GetAccountBookInfos.builder()
                .accountBooks(accountBookInfosByMemberId).build();
    }

    @Transactional
    public void createAccountBookPrivate(Member member, CreateRequestPrivate createRequest) {
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);
        for (String cat : defaultCategories) {
            Category category = Category.builder()
                    .accountBook(accountBook)
                    .name(cat).build();
            categoryRepository.save(category);
        }
    }

    @Transactional
    public SimpleAccountBookInfo createAccountBook(Long id, AccountBookCreateRequest accountBookCreateRequest) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = accountBookCreateRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);


        categoryService.createCategories(accountBookCreateRequest.getCategories(), accountBook);

        for (String email : accountBookCreateRequest.getEmails()) {
            InvitationInfo info = InvitationInfo.builder()
                    .id(accountBook.getId())
                    .memberName(member.getUserName())
                    .accountBookName(accountBook.getName())
                    .build();

            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveError();
            }
        }
        return accountBook.toInfoDto();
    }

    @Transactional
    public Invited createInvitation(Long id, GetInvitations invitations) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = accountBookRepository.findById(invitations.getId()).orElseThrow(NotFoundByIdException::new);
        long result = 0L;
        for (String email : invitations.getEmails()) {
            InvitationInfo info = InvitationInfo.builder()
                    .id(accountBook.getId())
                    .memberName(member.getUserName())
                    .accountBookName(accountBook.getName())
                    .build();
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveError();
            }
            result += 1;
        }
        return Invited.builder().invited(result).build();
    }

    @Transactional
    public InvitationReplyResult invitationReply(Long id, InvitationReply invitationReply) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        log.info(invitationReply.toString());
        if (invitationReply.getJoin()) {
            if (!accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(member.getId(), invitationReply.getId())) {
                AccountBook accountBook = accountBookRepository.findById(invitationReply.getId()).orElseThrow(NotFoundByIdException::new);
                AccountBookMember bookMember = AccountBookMember.builder()
                        .accountBook(accountBook)
                        .member(member)
                        .accountBookAuthority(AccountBookAuthority.USER)
                        .getNotification(true).build();
                accountBookMemberRepository.save(bookMember);
            }
        }
        InvitationInfo build = InvitationInfo.builder()
                .accountBookName(invitationReply.getAccountBookName())
                .memberName(invitationReply.getMemberName())
                .id(invitationReply.getId())
                .build();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(member.getEmail(), objectMapper.writeValueAsString(build));
        return InvitationReplyResult.builder()
                .id(invitationReply.getId())
                .join(invitationReply.getJoin()).build();

    }

    @Transactional
    public CreateResult createItems(Long id, CreateItems createItems) {
        long r = 0L;
        long i = 0L;
        // 아이템 작성자
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        // 현재 작성하고 있는 가계부
        AccountBook accountBook = accountBookRepository.findById(createItems.getId()).orElseThrow(NotFoundByIdException::new);
        // 현재 작성하고 있는 가계부의 카테고리
        List<Category> categories = accountBook.getCategories();
        // 카테고리 이름 - 카테고리 객체
        HashMap<String, Category> categoryDict = new HashMap<>();
        // 다른 가계부들의 기타 카테고리 객체
        List<Category> otherAccountBookDefaultCategories = categoryRepository.findCategoriesByMemberIdAndName(id, "기타");
        // 다른 공유 가계부 객체
        List<AccountBook> accountBooks = accountBookRepository.findAccountBooksByMemberId(id);
        // 다른 가계부들 아이디 - 다른 가계부들 기타 카테고리 객체
        HashMap<Long, Category> defaultCategories = new HashMap<>();
        // 다른 가계부들 아이디 - 다른 가계부들 객체
        HashMap<Long, AccountBook> otherAccountBooks = new HashMap<>();
        // 카테고리 이름과 카테고리 객체 매핑
        for (Category cat : categories) {
            categoryDict.put(cat.getName(), cat);
        }
        // 다른 가계부들 아이디와 기타 카테고리 객체 매핑
        for (Category cat : otherAccountBookDefaultCategories) {
            defaultCategories.put(cat.getAccountBook().getId(), cat);
        }
        // 다른 가계부 객체 - 아이템 생성 요청 리퀘스트
        Map<AccountBook, List<Income>> addIncomeToOtherAccountBooks = new HashMap<>();
        Map<AccountBook, List<Record>> addRecordToOtherAccountBooks = new HashMap<>();
        // 다른 가계부 아이디와 다른 가계부 객체 매핑
        for (AccountBook ab : accountBooks) {
            otherAccountBooks.put(ab.getId(), ab);
            addIncomeToOtherAccountBooks.put(ab, new ArrayList<>());
            addRecordToOtherAccountBooks.put(ab, new ArrayList<>());
        }
        List<CreateItemResult> createResult = new ArrayList<>();
        for (CreateRecordOrIncomeDto dto : createItems.getCreateRequest()) {
            String[] split = dto.getDate().split("-");
            Date date = Date.builder()
                    .year(Long.parseLong(split[0]))
                    .month(Long.parseLong(split[1]))
                    .day(Long.parseLong(split[2])).build();
            Category category = categoryDict.get(dto.getCategory());
            if (dto.getIsIncome()) {
                // 수입 생성
                CreateItemResult createItemResult = incomeService.addItemToAccountBook(dto, accountBook, member, date, category);
                createResult.add(createItemResult);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = otherAccountBooks.get(accountBookId);
                    Category defaultCategory = defaultCategories.get(accountBookId);
                    List<Income> addIncomeToAccountBooks = addIncomeToOtherAccountBooks.get(sharedAccountBook);

                    addIncomeToAccountBooks.add(
                            Income.builder()
                                    .account(dto.getAccount())
                                    .value(dto.getValue())
                                    .accountBook(sharedAccountBook)
                                    .memo(dto.getMemo())
                                    .category(defaultCategory)
                                    .creator(member)
                                    .payment(dto.getPayment())
                                    .date(date).build());
                }
                i += 1;

            } else {
                // 지출 생성
                CreateItemResult createItemResult = recordService.addItemToAccountBook(dto, accountBook, member, date, category);
                createResult.add(createItemResult);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = otherAccountBooks.get(accountBookId);
                    Category defaultCategory = defaultCategories.get(accountBookId);
                    List<Record> addRecordToAccountBooks = addRecordToOtherAccountBooks.get(sharedAccountBook);
                    addRecordToAccountBooks.add(
                            Record.builder()
                                    .account(dto.getAccount())
                                    .spend(dto.getValue())
                                    .accountBook(sharedAccountBook)
                                    .memo(dto.getMemo())
                                    .category(defaultCategory)
                                    .creator(member)
                                    .payment(dto.getPayment())
                                    .date(date).build());
                }
                r += 1;


            }

        }

        for (AccountBook ab : accountBooks) {
            incomeService.addIncomeToOtherAccountBooks(addIncomeToOtherAccountBooks.get(ab));
            recordService.addRecordToOtherAccountBooks(addRecordToOtherAccountBooks.get(ab));
        }

        return CreateResult.builder()
                .income(i)
                .record(r)
                .created(createResult).build();
    }

    @Transactional
    public AccountBookCategories getAccountBookCategories(Long id) {
        return AccountBookCategories.builder()
                .categories(accountBookRepository.findAccountBookCategoryInfoById(id)).build();
    }

    @Transactional
    public AccountBookMembers getAccountBookMembers(Long id) {
        return AccountBookMembers.builder()
                .members(accountBookRepository.findAccountBookMemberInfoById(id)).build();
    }


    @Transactional
    public CategoryCreateResult createCategory(Long memberId, CategoryCreateRequest createRequest) {
        Long id = createRequest.getId();
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (!accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, id)) {
            throw new UnauthorizedAccountBookCategoryCreateException();
        }
        List<String> result = new ArrayList<>();
        for (String cat : createRequest.getCategories()) {
            if (!categoryRepository.existsByAccountBookIdAndName(id, cat)) {
                categoryService.addCategoryToAccountBook(accountBook, cat);
                result.add(cat);
            }
        }
        return CategoryCreateResult.builder()
                .id(id)
                .created(result).build();
    }

    @Transactional
    public AccountBookIncomesAndRecords getAccountBookItems(Long id, Long year, Long month) {

        return AccountBookIncomesAndRecords.builder()
                .incomes(accountBookRepository.getMonthlyIncome(id, year, month))
                .records(accountBookRepository.getMonthlyRecord(id, year, month)).build();
    }


    @Transactional
    public RecordAndIncomeDetails getAccountBookItemDetails(Long id, Long year, Long month, Long day) {
        return RecordAndIncomeDetails
                .builder()
                .records(recordRepository.findByAccountBookId(id, year, month, day))
                .incomes(incomeRepository.findByAccountBookId(id, year, month, day))
                .build();
    }

    @Transactional
    public GetAccountBookAssets getAccountBookAssets(Long id, Long year, Long month) {
        return GetAccountBookAssets.builder()
                .budget(budgetRepository.getMonthlySumByAccountBookId(id, year, month))
                .income(incomeRepository.getMonthlySumByAccountBookId(id, year, month))
                .record(recordRepository.getMonthlySumByAccountBookId(id, year, month))
                .build();
    }


    @Transactional
    public AddIncomeResult addIncome(Long memberId, AddIncomeRequest request) {
        AccountBook accountBook = accountBookRepository.findById(request.getId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(accountBook, request.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        String[] split = request.getDate().split("-");
        Date date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();
        return incomeService.addSingleIncomeToAccountBook(request, category, date, accountBook, member);
    }

    @Transactional
    public AddRecordResult addRecord(Long memberId, AddRecordRequest request) {
        AccountBook accountBook = accountBookRepository.findById(request.getId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(accountBook, request.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        String[] split = request.getDate().split("-");
        Date date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();
        return recordService.addSingleItemToAccountBook(request, category, date, accountBook, member);
    }


    @Transactional
    public BudgetInfoQ addBudget(Map<String, Object> dto) {
        CreateBudgetDto createBudgetDto = objectMapper.convertValue(dto, CreateBudgetDto.class);

        return budgetService.addBudgetToAccountBook(createBudgetDto);
    }

    @Transactional
    public AddScheduleResult addSchedule(Long memberId, AddSchedules addSchedules) throws JsonProcessingException {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = accountBookRepository.findById(addSchedules.getId()).orElseThrow(NotFoundByIdException::new);
        return scheduleService.addSchedule(member, accountBook, addSchedules.getSchedules());
    }

    @Transactional
    public GetAccountBookSchedules getAccountBookSchedules(Long memberId, Long accountBookId) {
        if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBookId)) {

            return scheduleService.getAccountBookSchedules(accountBookId);
        } else {
            throw new UnauthorizedAccountBookAccessException();
        }
    }

    @Transactional
    public String deleteAccountBook(Long id, Long memberId) {
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, id)) {
            accountBookRepository.delete(accountBook);
        } else {
            throw new InvalidAccountBookDeleteRequest();
        }
        return "DELETED";
    }

}
