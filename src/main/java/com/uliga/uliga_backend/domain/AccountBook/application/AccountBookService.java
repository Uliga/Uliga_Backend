package com.uliga.uliga_backend.domain.AccountBook.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookAccessException;
import com.uliga.uliga_backend.domain.AccountBook.exception.UnauthorizedAccountBookCategoryCreateException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBookAuthority;
import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
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
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookService {

    private final AccountBookRepository accountBookRepository;
    private final AccountBookMemberRepository accountBookMemberRepository;
    private final MemberRepository memberRepository;
    private final IncomeRepository incomeRepository;
    private final RecordRepository recordRepository;
    private final ScheduleRepository scheduleRepository;
    private final BudgetRepository budgetRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final CategoryRepository categoryRepository;

    private final CategoryService categoryService;
    private final RecordService recordService;
    private final IncomeService incomeService;
    private final ScheduleService scheduleService;
    private final BudgetService budgetService;

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
    public SimpleAccountBookInfo createAccountBookPrivate(Long id, CreateRequestPrivate createRequest) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);
        // 카테고리 생성 authService에서 해줌
        return accountBook.toInfoDto();
    }

    @Transactional
    public SimpleAccountBookInfo createAccountBook(Long id, CreateRequest createRequest) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = createRequest.toEntity();
        accountBookRepository.save(accountBook);
        AccountBookMember bookMember = AccountBookMember.builder()
                .accountBook(accountBook)
                .member(member)
                .accountBookAuthority(AccountBookAuthority.ADMIN)
                .getNotification(true).build();
        accountBookMemberRepository.save(bookMember);

        categoryService.createCategories(createRequest.getCategories(), accountBook);

        for (String email : createRequest.getEmails()) {
            InvitationInfo info = InvitationInfo.builder()
                    .id(accountBook.getId())
                    .memberName(member.getUserName())
                    .accountBookName(accountBook.getName()).build();

            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            setOperations.add(email, objectMapper.writeValueAsString(info));
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
                    .accountBookName(accountBook.getName()).build();
            SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
            setOperations.add(email, objectMapper.writeValueAsString(info));
            result += 1;
        }
        return Invited.builder().invited(result).build();
    }

    @Transactional
    public InvitationReplyResult invitationReply(Long id, InvitationReply invitationReply) throws JsonProcessingException {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = accountBookRepository.findById(invitationReply.getId()).orElseThrow(NotFoundByIdException::new);
        if (invitationReply.getJoin()) {
            AccountBookMember bookMember = AccountBookMember.builder()
                    .accountBook(accountBook)
                    .member(member)
                    .accountBookAuthority(AccountBookAuthority.USER)
                    .getNotification(true).build();
            accountBookMemberRepository.save(bookMember);
        }
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        InvitationInfo info = InvitationInfo.builder()
                .id(accountBook.getId())
                .memberName(member.getUserName())
                .accountBookName(accountBook.getName()).build();
        setOperations.remove(member.getEmail(), objectMapper.writeValueAsString(info));
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
        HashMap<String, Category> categoryDict = new HashMap<>();
        // 다른 가계부들의 기타 카테고리 객체
        List<Category> otherAccountBookDefaultCategories = categoryRepository.findCategoriesByMemberIdAndName(id, "기타");
        // 다른 공유 가계부 객체
        List<AccountBook> accountBooks = accountBookRepository.findAccountBooksByMemberId(id);

        HashMap<Long, Category> defaultCategories = new HashMap<>();
        HashMap<Long, AccountBook> otherAccountBooks = new HashMap<>();
        for (Category cat : categories) {
            categoryDict.put(cat.getName(), cat);
        }
        for (Category cat : otherAccountBookDefaultCategories) {
            defaultCategories.put(cat.getAccountBook().getId(), cat);
        }
        for (AccountBook ab : accountBooks) {
            otherAccountBooks.put(ab.getId(), ab);
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
                    incomeService.addItemToSharedAccountBook(dto, sharedAccountBook, member, date, defaultCategory);
                }
                i += 1;

            } else {
                // 지출 생성
                CreateItemResult createItemResult = recordService.addItemToAccountBook(dto, accountBook, member, date, category);
                createResult.add(createItemResult);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = otherAccountBooks.get(accountBookId);
                    Category defaultCategory = defaultCategories.get(accountBookId);
                    recordService.addItemToSharedAccountBook(dto, sharedAccountBook, member, date, defaultCategory);
                }
                r += 1;


            }

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
    public AccountBookItems getAccountBookItems(Long id, Long year, Long month, Long startDay, Long endDay) {

        return AccountBookItems.builder()
                .incomes(incomeRepository.findByAccountBookId(id, year, month))
                .records(recordRepository.findByAccountBookId(id, year, month))
                .schedules(scheduleRepository.findByAccountBookId(id)).build();
    }

    @Transactional
    public GetAccountBookAssets getAccountBookAssets(Long id,Long year, Long month) {
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


}
