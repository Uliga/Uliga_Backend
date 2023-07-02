package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.exception.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataMapper;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataRepository;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Category.application.CategoryService;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
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
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final AccountBookDataRepository accountBookDataRepository;
    private final AccountBookDataMapper accountBookDataMapper;
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

    /**
     * 가계부 정보 조회
     * @param id 가계부 아이디
     * @param memberId 멤버 아이디
     * @return 가계부 정보
     */
    @Transactional(readOnly = true)
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

    /**
     * 멤버 가계부 조회
     * @param id 멤버 아이디
     * @return 멤버 가계부 정보 리스트
     */
    @Transactional(readOnly = true)
    public GetAccountBookInfos getMemberAccountBook(Long id) {
        List<AccountBookInfo> result = new ArrayList<>();
        List<AccountBookInfoQ> accountBookInfosByMemberId = accountBookRepository.findAccountBookInfosByMemberId(id);
        for (AccountBookInfoQ accountBookInfoQ : accountBookInfosByMemberId) {
            AccountBookInfo build = AccountBookInfo.builder().info(accountBookInfoQ)
                    .members(accountBookRepository.findAccountBookMemberInfoById(accountBookInfoQ.getAccountBookId()))
                    .numberOfMember(accountBookRepository.getMemberNumberByAccountBookId(accountBookInfoQ.getAccountBookId()))
                    .categories(accountBookRepository.findAccountBookCategoryInfoById(accountBookInfoQ.getAccountBookId())).build();
            result.add(build);
        }

        return new GetAccountBookInfos(result);
    }


    /**
     * 멤버 개인 가계부 생성
     *
     * @param member        멤버
     * @param createRequest 가계부 생성 요청
     */
    @Transactional
    public void createAccountBookPrivate(Member member, CreateRequestPrivate createRequest) {
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        member.setPrivateAccountBook(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .avatarUrl("default")
                .getNotification(true).build();

        accountBookMemberRepository.save(bookMember);
        for (String cat : defaultCategories) {
            Category category = Category.builder()
                    .accountBook(accountBook)
                    .name(cat).build();
            categoryRepository.save(category);
        }
    }

    /**
     * 가계부 생성 요청
     * @param id 멤버 아이디
     * @param accountBookCreateRequest 가계부 생성 요청
     * @return 생성한 가계부 정보
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public SimpleAccountBookInfo createAccountBook(Long id, AccountBookCreateRequest accountBookCreateRequest) throws JsonProcessingException {

        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        AccountBook accountBook = accountBookCreateRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .avatarUrl("default")
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);


        categoryService.createCategories(accountBookCreateRequest.getCategories(), accountBook);

        for (String email : accountBookCreateRequest.getEmails()) {
            InvitationInfo info = new InvitationInfo(accountBook.getId(), member.getUserName(), accountBook.getName());
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveErrorWithCreation(accountBook.toInfoDto());
            }
        }
        return accountBook.toInfoDto();
    }

    /**
     * 멤버 초대
     * @param id 초대자 ID
     * @param invitations 초대하려는 사람들
     * @return 초대된 사람들 수
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public Invited createInvitation(Long id, GetInvitations invitations) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        AccountBook accountBook = accountBookRepository.findById(invitations.getId()).orElseThrow(() -> new NotFoundByIdException("가계부 초대에 아이디 값이 없습니다"));
        long result = 0L;
        for (String email : invitations.getEmails()) {
            InvitationInfo info = new InvitationInfo(accountBook.getId(), member.getUserName(), accountBook.getName());
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            try {

                setOperations.add(email, objectMapper.writeValueAsString(info));
            } catch (RedisSystemException e) {
                throw new InvitationSaveError();
            }
            result += 1;
        }
        return new Invited(result);
    }

    /**
     * 초대에 대한 응답
     * @param id 응답자 아이디
     * @param invitationReply 초대에 대한 응답
     * @return 응답 결과
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public InvitationReplyResult invitationReply(Long id, InvitationReply invitationReply) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        log.info(invitationReply.toString());
        if (invitationReply.getJoin()) {
            if (!accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(member.getId(), invitationReply.getId())) {
                AccountBook accountBook = accountBookRepository.findById(invitationReply.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
                AccountBookMember bookMember = AccountBookMember.builder()
                        .accountBook(accountBook)
                        .member(member)
                        .accountBookAuthority(AccountBookAuthority.USER)
                        .avatarUrl("default")
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
        return new InvitationReplyResult(invitationReply.getId(), invitationReply.getJoin());

    }

    /**
     * 가계부에 수입/지출 추가
     * @param id 생성하려는 멤버 아이디
     * @param createItems 생성하려는 데이터들
     * @return 생성 결과
     */
    @Transactional
    public AccountBookDataDTO.CreateResult createItems(Long id, AccountBookDataDTO.CreateItems createItems) {
        long r = 0L;
        long i = 0L;
        // 아이템 작성자
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        // 현재 작성하고 있는 가계부
        AccountBook accountBook = accountBookRepository.findById(createItems.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
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
        List<AccountBookDataDTO.CreateItemResult> createResult = new ArrayList<>();
        for (AccountBookDataDTO.CreateRecordOrIncomeDto dto : createItems.getCreateRequest()) {
            String[] split = dto.getDate().split("-");
            Date date = Date.builder()
                    .year(Long.parseLong(split[0]))
                    .month(Long.parseLong(split[1]))
                    .day(Long.parseLong(split[2])).build();
            Category category = categoryDict.get(dto.getCategory());
            if (dto.getIsIncome()) {
                // 수입 생성
                AccountBookDataDTO.CreateItemResult createItemResult = incomeService.addItemToAccountBook(dto, accountBook, member, date, category);
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
                AccountBookDataDTO.CreateItemResult createItemResult = recordService.addItemToAccountBook(dto, accountBook, member, date, category);
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

        return new AccountBookDataDTO.CreateResult(i, r, createResult);
    }

    /**
     * 가계부 카테고리 정보 조회
     * @param id 가계부 아이디
     * @return 가계부 카테고리 정보
     */
    @Transactional(readOnly = true)
    public CategoryDTO.AccountBookCategories getAccountBookCategories(Long id) {
        return new CategoryDTO.AccountBookCategories(accountBookRepository.findAccountBookCategoryInfoById(id));
    }

    /**
     * 가계부 멤버 조회
     * @param id 가계부 아이디
     * @return 가계부 멤버 정보
     */
    @Transactional(readOnly = true)
    public AccountBookMembers getAccountBookMembers(Long id) {
        return new AccountBookMembers(accountBookRepository.findAccountBookMemberInfoById(id));
    }

    /**
     * 가계부에 카테고리 추가
     * @param memberId 멤버 아이디
     * @param createRequest 카테고리 생성 요청
     * @return 카테고리 생성 요청 결과
     */
    @Transactional
    public CategoryDTO.CategoryCreateResult createCategory(Long memberId, CategoryDTO.CategoryCreateRequest createRequest) {
        Long id = createRequest.getId();
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
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
        return new CategoryDTO.CategoryCreateResult(id, result);
    }

    /**
     * 가계부 해당 달 날짜별 수입/지출 총합 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 월
     * @return 해당 달 날짜별 수입/지출 총합
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.AccountBookDataDailySum getAccountBookItems(Long id, Long year, Long month) {

        return AccountBookDataDTO.AccountBookDataDailySum.builder()
                .incomes(accountBookRepository.getMonthlyIncome(id, year, month))
                .records(accountBookRepository.getMonthlyRecord(id, year, month)).build();
    }

    /**
     * 가계부 기간 내 수입/지출 정보 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 달
     * @param day 날짜
     * @return 수입&지출 정보 리스트
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.RecordAndIncomeDetails getAccountBookItemDetails(Long id, Long year, Long month, Long day) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", id);
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        return new AccountBookDataDTO.RecordAndIncomeDetails(accountBookDataMapper.findAccountBookDataOrderByValue(map));
    }

    /**
     * 가계부 수입/지출 삭제
     * @param deleteItemRequest 아이템 삭제 요청
     */
    @Transactional
    public void deleteAccountBookItems(AccountBookDataDTO.DeleteItemRequest deleteItemRequest) {

        accountBookDataRepository.deleteAllById(deleteItemRequest.getDeleteIds());
    }

    /**
     * 가계부 예산, 지출, 수입 총합 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 월
     * @return 월별 예산, 지출, 수입 총합 조회
     */
    @Transactional(readOnly = true)
    public BudgetDTO.GetAccountBookAssets getAccountBookAssets(Long id, Long year, Long month) {
        MonthlySumQ budget = budgetRepository.getMonthlySumByAccountBookId(id, year, month).orElse(new MonthlySumQ(0L));
        MonthlySumQ record = recordRepository.getMonthlySumByAccountBookId(id, year, month).orElse(new MonthlySumQ(0L));
        MonthlySumQ income = incomeRepository.getMonthlySumByAccountBookId(id, year, month).orElse(new MonthlySumQ(0L));

        return BudgetDTO.GetAccountBookAssets.builder()
                .budget(budget)
                .income(income)
                .record(record)
                .build();
    }

    /**
     * 가계부에 수입 추가
     * @param memberId 멤버 아이디
     * @param request 수입 추가 요청
     * @return 추가 결과
     */
    @Transactional
    public AccountBookDataDTO.AddIncomeResult addIncome(Long memberId, AccountBookDataDTO.AddIncomeRequest request) {
        AccountBook accountBook = accountBookRepository.findById(request.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        Category category = categoryRepository.findByAccountBookAndName(accountBook, request.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        String[] split = request.getDate().split("-");
        Date date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();
        return incomeService.addSingleIncomeToAccountBook(request, category, date, accountBook, member);
    }

    /**
     * 가계부에 지출 추가
     * @param memberId 멤버 아이디
     * @param request 지출 추가 요청
     * @return 지출 추가 결과
     */
    @Transactional
    public AccountBookDataDTO.AddRecordResult addRecord(Long memberId, AccountBookDataDTO.AddRecordRequest request) {
        AccountBook accountBook = accountBookRepository.findById(request.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        Category category = categoryRepository.findByAccountBookAndName(accountBook, request.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        String[] split = request.getDate().split("-");
        Date date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();
        return recordService.addSingleItemToAccountBook(request, category, date, accountBook, member);
    }

    /**
     * 가계부 예산 설정
     * @param dto 생성 요청
     * @return 예산 정보
     */
    @Transactional
    public BudgetInfoQ addBudget(Map<String, Object> dto) {
        CreateBudgetDto createBudgetDto = objectMapper.convertValue(dto, CreateBudgetDto.class);

        return budgetService.addBudgetToAccountBook(createBudgetDto);
    }

    /**
     * 가계부 금융 일정 추가
     * @param memberId 멤버 아이디
     * @param addSchedules 추가할 일정들
     * @return 추가 결과
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public ScheduleDTO.AddScheduleResult addSchedule(Long memberId, ScheduleDTO.AddSchedules addSchedules) throws JsonProcessingException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        AccountBook accountBook = accountBookRepository.findById(addSchedules.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        return scheduleService.addSchedule(member, accountBook, addSchedules.getSchedules());
    }

    /**
     * 가계부 금융 일정 조회
     * @param memberId 멤버 아이디
     * @param accountBookId 가계부 아이디
     * @return 가계부 금융 일정 조회
     */
    @Transactional(readOnly = true)
    public ScheduleDTO.GetAccountBookSchedules getAccountBookSchedules(Long memberId, Long accountBookId) {
        if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBookId)) {

            return scheduleService.getAccountBookSchedules(accountBookId);
        } else {
            throw new UnauthorizedAccountBookAccessException();
        }
    }

    /**
     * 가계부 삭제
     * @param id 가계부 아이디
     * @param memberId 멤버 아이디
     * @return DELETED
     */
    @Transactional
    public String deleteAccountBook(Long id, Long memberId) {
        AccountBook accountBook = accountBookRepository.findById(id).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        if (accountBookMemberRepository.existsAccountBookMemberByMemberIdAndAccountBookId(memberId, id)) {
            accountBookRepository.delete(accountBook);
        } else {
            throw new InvalidAccountBookDeleteRequest();
        }
        return "DELETED";
    }

    /**
     * 가계부 내역 조회
     * @param accountBookId 가계부 아이디
     * @param categoryId 카테고리 아이디
     * @param year 년도
     * @param month 월
     * @param pageable 페이징 정보
     * @return 가계부 내역
     */
    @Transactional(readOnly = true)
    public Page<AccountBookDataQ> getAccountBookHistory(Long accountBookId, Long categoryId, Long year, Long month, Pageable pageable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("categoryId", categoryId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());
        List<AccountBookDataQ> accountBookData = accountBookDataMapper.findAccountBookData(map);
        List<Long> counted = accountBookDataMapper.countQueryForAccountBookHistory(map);
        return new PageImpl<>(accountBookData, pageable, counted.size());

    }

    /**
     * 가계부 데이터 삭제
     * @param dataDeleteRequest 삭제 요청
     */
    @Transactional
    public void deleteAccountBookData(AccountBookDataDTO.AccountBookDataDeleteRequest dataDeleteRequest) {
        accountBookDataRepository.deleteAllById(dataDeleteRequest.getIds());
    }

    /**
     * 가계부 정보 업데이트 요청
     * @param memberId 멤버 아이디
     * @param accountBookId 가계부 아이디
     * @param map 업데이트 요청
     * @return 업데이트 결과
     * @throws JsonProcessingException Json 처리 예외
     */
    @Transactional
    public AccountBookUpdateRequest updateAccountBookInfo(Long memberId, Long accountBookId, Map<String, Object> map) throws JsonProcessingException {
        AccountBookUpdateRequest request = objectMapper.convertValue(map, AccountBookUpdateRequest.class);
        AccountBook accountBook = accountBookRepository.findById(accountBookId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        AccountBookMember accountBookMember = accountBookMemberRepository.findAccountBookMemberByMemberIdAndAccountBookId(memberId, accountBookId).orElseThrow(UnauthorizedAccountBookAccessException::new);
        List<Category> categoriesByAccountBookId = categoryRepository.findCategoriesByAccountBookId(accountBookId);
        HashMap<String, Category> hashMap = new HashMap<>();
        for (Category category : categoriesByAccountBookId) {
            hashMap.put(category.getName(), category);
        }

        if (request.getAvatarUrl() != null) {
            accountBookMember.setAvatarUrl(request.getAvatarUrl());
        }

        if (request.getMembers() != null) {
            createInvitation(memberId, GetInvitations.builder().id(accountBookId).emails(request.getMembers()).build());
        }

        if (request.getName() != null) {
            accountBook.setName(request.getName());
        }

        if (request.getCategories() != null) {
            List<String> createCategories = new ArrayList<>();
            for (String category : request.getCategories()) {
                if (!hashMap.containsKey(category)) {
                    createCategories.add(category);
                }
            }
            for (Category category : categoriesByAccountBookId) {
                if (!request.getCategories().contains(category.getName())) {
                    categoryRepository.delete(category);
                }
            }
            createCategory(memberId, CategoryDTO.CategoryCreateRequest.builder().id(accountBookId).categories(createCategories).build());
        }

        if (request.getRelationship() != null) {
            accountBook.setRelationShip(request.getRelationship());
        }
        return request;
    }

    /**
     * 가계부 분석 - 날짜별 지출 총액 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 날짜별 지출 총합
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.AccountBookDailyRecord getAccountBookDailyRecord(Long id, Long year, Long month) {
        List<DailyValueQ> monthlyRecord = accountBookRepository.getMonthlyRecord(id, year, month);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Math.toIntExact(year), Math.toIntExact(month) - 1, 1);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int index = 0;
        List<DailyValueQ> result = new ArrayList<>();
        for (int i = 1; i <= actualMaximum; i++) {
            if (index < monthlyRecord.size()) {
                if (monthlyRecord.get(index).getDay().equals((long) i)) {
                    result.add(monthlyRecord.get(index));
                    index += 1;
                } else {
                    result.add(new DailyValueQ((long) i, 0L));
                }
            } else {
                result.add(new DailyValueQ((long) i, 0L));

            }
        }
        List<MonthlyCompareQ> monthlyCompareInDailyAnalyze = accountBookRepository.getMonthlyCompareInDailyAnalyze(id, year, month);
        if (monthlyCompareInDailyAnalyze.size() == 2) {
            Long diff = monthlyCompareInDailyAnalyze.get(0).getValue() - monthlyCompareInDailyAnalyze.get(1).getValue();
            return new AccountBookDataDTO.AccountBookDailyRecord(result, monthlyCompareInDailyAnalyze.get(0).getValue(), diff);
        } else {
            if (monthlyCompareInDailyAnalyze.size() == 0) {
                return new AccountBookDataDTO.AccountBookDailyRecord(result, 0L, null);
            } else {
                return new AccountBookDataDTO.AccountBookDailyRecord(result, monthlyCompareInDailyAnalyze.get(0).getValue(), null);
            }

        }

    }

    /**
     * 가계부 분석 - 카테고리별 지출 총합 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 분석 정보
     */
    @Transactional(readOnly = true)
    public CategoryDTO.AccountBookCategoryAnalyze getAccountBookCategoryAnalyze(Long id, Long year, Long month) {
        List<AccountBookCategoryAnalyzeQ> categoryAnalyze = accountBookRepository.findAccountBookCategoryAnalyze(id, year, month);
        Optional<MonthlySumQ> monthlySum = recordRepository.getMonthlySumByAccountBookId(id, year, month);
        if (monthlySum.isPresent()) {
            MonthlySumQ monthlySumQ = monthlySum.get();
            Long compare = 0L;
            for (AccountBookCategoryAnalyzeQ analyzeQ : categoryAnalyze) {
                compare += analyzeQ.getValue();
            }
            if (compare.equals(monthlySumQ.getValue())) {
                return new CategoryDTO.AccountBookCategoryAnalyze(categoryAnalyze, monthlySumQ.getValue());
            } else {
                AccountBookCategoryAnalyzeQ built = new AccountBookCategoryAnalyzeQ(null, "그 외", monthlySumQ.getValue() - compare);
                categoryAnalyze.add(built);
                return new CategoryDTO.AccountBookCategoryAnalyze(categoryAnalyze, monthlySumQ.getValue());
            }
        } else {
            List<AccountBookCategoryInfoQ> accountBookCategoryInfoById = accountBookRepository.findAccountBookCategoryAnalyze(id);
            List<AccountBookCategoryAnalyzeQ> result = new ArrayList<>();
            for (AccountBookCategoryInfoQ accountBookCategoryInfoQ : accountBookCategoryInfoById) {
                AccountBookCategoryAnalyzeQ built = new AccountBookCategoryAnalyzeQ(accountBookCategoryInfoQ.getId(), accountBookCategoryInfoQ.getLabel(), 0L);
                result.add(built);

            }
            return new CategoryDTO.AccountBookCategoryAnalyze(result, 0L);
        }


    }

    /**
     * 가계부 분석 - 고정 지출 조회
     * @param accountBookId 가계부 아이디
     * @param memberId 멤버 아이디
     * @return 고정 지출 분석 결과
     */
    @Transactional(readOnly = true)
    public ScheduleDTO.AccountScheduleAnalyze getAccountBookScheduleAnalyze(Long accountBookId, Long memberId) {
        return ScheduleDTO.AccountScheduleAnalyze.builder().schedules(scheduleService.findAnalyze(accountBookId, memberId)).sum(accountBookRepository.getMonthlyScheduleValue(accountBookId, memberId).getValue()).build();
    }

    /**
     * 가계부 분석 - 전월 대비 지출 양 분석
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 비교 결과
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.MonthlyCompare getAccountBookMonthlyCompare(Long accountBookId, Long year, Long month) {
        List<MonthlyCompareQ> monthlyCompare = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (int i = 2; i > -1; i--) {
            calendar.set(Math.toIntExact(year), month.intValue() - i, 1);
            Optional<MonthlyCompareQ> monthly = accountBookRepository.getMonthlyCompare(accountBookId, year, month - i);
            if (monthly.isPresent()) {
                monthlyCompare.add(monthly.get());
            } else {
                MonthlyCompareQ build = MonthlyCompareQ.builder().year((long) calendar.get(Calendar.YEAR)).month((long) calendar.get(Calendar.MONTH)).value(0L).build();
                monthlyCompare.add(build);
            }
        }



//        return MonthlyCompare.builder().compare(monthlyCompare).build();
        return new AccountBookDataDTO.MonthlyCompare(monthlyCompare);
    }

    /**
     * 가계부 분석 - 내역 조회
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @param pageable 페이징 정보
     * @param category 카테고리
     * @return 내역
     */
    @Transactional(readOnly = true)
    public Page<AccountBookDataQ> getAccountBookMonthlyRecord(Long accountBookId, Long year, Long month, Pageable pageable, String category) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());
        map.put("type", "RECORD");
        if (Objects.equals(category, "그 외")) {
            List<String> extraAccountBookCategory = accountBookRepository.findExtraAccountBookCategory(accountBookId, year, month);
            if (extraAccountBookCategory.size() == 0) {

                extraAccountBookCategory.add("기타");
            }
            map.put("category", extraAccountBookCategory);

            List<AccountBookDataQ> accountBookData = accountBookDataMapper.findExtraAccountBookDataAnalyze(map);
            List<Long> counted = accountBookDataMapper.countQueryForExtraAccountBookDataAnalyze(map);
            return new PageImpl<>(accountBookData, pageable, counted.size());


        } else {
            map.put("category", category);

            List<AccountBookDataQ> accountBookData = accountBookDataMapper.findAccountBookDataAnalyze(map);
            List<Long> counted = accountBookDataMapper.countQueryForAccountBookDataAnalyze(map);
            return new PageImpl<>(accountBookData, pageable, counted.size());
        }


    }

    /**
     * 가계부 분석 - 예산과 비교
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 비교 결과
     */
    @Transactional(readOnly = true)
    public BudgetDTO.BudgetCompare getBudgetCompare(Long accountBookId, Long year, Long month) {

        Optional<MonthlySumQ> recordSum = recordRepository.getMonthlySumByAccountBookId(accountBookId, year, month);
        Optional<MonthlySumQ> budgetSum = budgetRepository.getMonthlySumByAccountBookId(accountBookId, year, month);
        if (recordSum.isPresent() && budgetSum.isPresent()) {
            MonthlySumQ record = recordSum.get();
            MonthlySumQ budget = budgetSum.get();
            return new BudgetDTO.BudgetCompare(record.getValue(), budget.getValue(), budget.getValue() - record.getValue());
        } else if (budgetSum.isPresent()) {
            MonthlySumQ budget = budgetSum.get();
            return new BudgetDTO.BudgetCompare(0L, budget.getValue(), budget.getValue());
        } else if (recordSum.isPresent()) {
            MonthlySumQ record = recordSum.get();
            return new BudgetDTO.BudgetCompare(record.getValue(), 0L, -record.getValue());
        } else {
            return new BudgetDTO.BudgetCompare(0L, 0L, 0L);
        }
    }

    /**
     * 가계부 분석 - 주차별 조회
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @param startDay 분석 시작 일
     * @return 주차별 분석 결과
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.AccountBookWeeklyRecord getAccountBookWeeklyRecord(Long accountBookId, Long year, Long month, Long startDay) {
        Long totalSum = 0L;
        List<AccountBookDataDTO.WeeklySum> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Math.toIntExact(year), Math.toIntExact(month) - 1, 1);
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        while (startDay <= actualMaximum) {
            Optional<WeeklySumQ> weeklyRecordSum = accountBookRepository.getWeeklyRecordSum(accountBookId, year, month, startDay, startDay + 7);
            long endDay;
            if (startDay + 6 <= actualMaximum) {
                endDay = startDay + 6;
            } else {
                endDay = actualMaximum;
            }
            if (weeklyRecordSum.isPresent()) {
                WeeklySumQ weeklySumQ = weeklyRecordSum.get();
                AccountBookDataDTO.WeeklySum weeklySum = AccountBookDataDTO.WeeklySum.builder().startDay(startDay ).endDay(endDay).value(weeklySumQ.getValue()).build();
                result.add(weeklySum);
                totalSum += weeklySum.getValue();
            } else {
                AccountBookDataDTO.WeeklySum weeklySum = AccountBookDataDTO.WeeklySum.builder().startDay(startDay).endDay(endDay).value(0L).build();
                result.add(weeklySum);
            }
            startDay += 7;

        }
        return new AccountBookDataDTO.AccountBookWeeklyRecord(result, totalSum);
    }

    /**
     * 가계부 분석 - 사용자 지정 날짜 기간동안 가계부 내역 조회
     * @param id 가계부 아이디
     * @param year 년도
     * @param month 달
     * @param startDay 시작일
     * @param endDay 종료일
     * @param category 카테고리
     * @param pageable 페이지 정보
     * @return 해당 기간 가계부 내역 데이터
     */
    @Transactional(readOnly = true)
    public Page<AccountBookDataQ> getCustomAccountBookData(Long id, Long year, Long month, Long startDay, Long endDay, String category, Pageable pageable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", id);
        map.put("year", year);
        map.put("month", month);
        map.put("startDay", startDay);
        map.put("endDay", endDay);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());
        map.put("category", category);

        List<AccountBookDataQ> accountBookData = accountBookDataMapper.findCustomAccountBookData(map);
        List<Long> counted = accountBookDataMapper.countQueryForCustomAccountBookData(map);
        return new PageImpl<>(accountBookData, pageable, counted.size());
    }
}
