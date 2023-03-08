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
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.JoinTable.dao.AccountBookMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.InvitationInfo;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private final CategoryRepository categoryRepository;

    @Transactional
    public AccountBookInfo getSingleAccountBookInfo(Long id, Long memberId) {
        AccountBookInfoQ bookInfoById = accountBookRepository.findAccountBookInfoById(id, memberId);
        if (bookInfoById == null) {
            throw new UnauthorizedAccountBookAccessException();
        }
        return AccountBookInfo.builder()
                .info(bookInfoById)
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
        Category defaultCategory = Category.builder()
                .accountBook(accountBook)
                .name("미지정").build();
        categoryRepository.save(defaultCategory);
        for (String category : createRequest.getCategories()) {
            Category newCategory = Category.builder()
                    .accountBook(accountBook)
                    .name(category)
                    .build();
            categoryRepository.save(newCategory);
        }
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
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        AccountBook accountBook = accountBookRepository.findById(createItems.getId()).orElseThrow(NotFoundByIdException::new);
        List<CreateItemResult> createResult = new ArrayList<>();
        for (CreateRecordOrIncomeDto dto : createItems.getCreateRequest()) {
            String[] split = dto.getDate().split("-");
            Date date = Date.builder()
                    .year(Long.parseLong(split[0]))
                    .month(Long.parseLong(split[1]))
                    .day(Long.parseLong(split[2])).build();
            Category category = categoryRepository.findByAccountBookAndName(accountBook, dto.getCategory()).orElseThrow(CategoryNotFoundException::new);
            if (dto.getIsIncome()) {
                // 수입 생성
                Income build = Income.builder()
                        .payment(dto.getPayment())
                        .account(dto.getAccount())
                        .creator(member)
                        .accountBook(accountBook)
                        .value(dto.getValue())
                        .memo(dto.getMemo())
                        .date(date)
                        .category(category).build();
                incomeRepository.save(build);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = accountBookRepository.findById(accountBookId).orElseThrow(NotFoundByIdException::new);
                    Category defaultCategory = categoryRepository.findByAccountBookAndName(sharedAccountBook, "미지정").orElseThrow(CategoryNotFoundException::new);
                    Income sharedIncome = Income.builder()
                            .payment(dto.getPayment())
                            .account(dto.getAccount())
                            .creator(member)
                            .accountBook(sharedAccountBook)
                            .value(dto.getValue())
                            .memo(dto.getMemo())
                            .date(date)
                            .category(defaultCategory).build();
                    // TODO: 어라 근데 다른 가계부에 해당 카테고리가 없으면 어쩜??? 그냥 일단 비워둘까???
                    incomeRepository.save(sharedIncome);
                }
                i += 1;
                CreateItemResult itemResult = CreateItemResult.builder()
                        .id(build.getId())
                        .account(dto.getAccount())
                        .isIncome(true)
                        .category(category.getName())
                        .memo(dto.getMemo())
                        .payment(dto.getPayment())
                        .value(dto.getValue())
                        .year(date.getYear())
                        .month(date.getMonth())
                        .day(date.getDay())
                        .build();
                createResult.add(itemResult);

            } else {
                // 지출 생성
                Record build = Record.builder()
                        .account(dto.getAccount())
                        .creator(member)
                        .accountBook(accountBook)
                        .spend(dto.getValue())
                        .payment(dto.getPayment())
                        .date(date)
                        .category(category)
                        .memo(dto.getMemo())
                        .build();
                recordRepository.save(build);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = accountBookRepository.findById(accountBookId).orElseThrow(NotFoundByIdException::new);
                    Category defaultCategory = categoryRepository.findByAccountBookAndName(sharedAccountBook, "미지정").orElseThrow(CategoryNotFoundException::new);
                    Record sharedRecord = Record.builder()
                            .account(dto.getAccount())
                            .creator(member)
                            .accountBook(sharedAccountBook)
                            .spend(dto.getValue())
                            .payment(dto.getPayment())
                            .date(date)
                            .category(defaultCategory)
                            .memo(dto.getMemo())
                            .build();
                    recordRepository.save(sharedRecord);
                }
                r += 1;
                CreateItemResult itemResult = CreateItemResult.builder()
                        .id(build.getId())
                        .account(dto.getAccount())
                        .isIncome(false)
                        .category(category.getName())
                        .memo(dto.getMemo())
                        .payment(dto.getPayment())
                        .value(dto.getValue())
                        .year(date.getYear())
                        .month(date.getMonth())
                        .day(date.getDay())
                        .build();
                createResult.add(itemResult);


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
                Category newCategory = Category.builder()
                        .accountBook(accountBook)
                        .name(cat)
                        .build();
                categoryRepository.save(newCategory);
                result.add(cat);
            }
        }
        return CategoryCreateResult.builder()
                .id(id)
                .created(result).build();
    }

    @Transactional
    public AccountBookItems getAccountBookItems(Long id) {

        return AccountBookItems.builder()
                .incomes(incomeRepository.findByAccountBookId(id))
                .records(recordRepository.findByAccountBookId(id))
                .schedules(scheduleRepository.findByAccountBookId(id)).build();
    }

    @Transactional
    public UpdateCategoryResult updateRecordCategory(UpdateRecordCategory recordCategory) {
        Record record = recordRepository.findById(recordCategory.getRecordId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(record.getAccountBook(), recordCategory.getCategory()).orElseThrow(CategoryNotFoundException::new);
        String updateCategory = record.updateCategory(category);

        return UpdateCategoryResult.builder()
                .updateItemId(record.getId())
                .category(updateCategory)
                .build();
    }

    @Transactional
    public UpdateCategoryResult updateIncomeCategory(UpdateIncomeCategory incomeCategory) {
        Income income = incomeRepository.findById(incomeCategory.getIncomeId()).orElseThrow(NotFoundByIdException::new);
        Category category = categoryRepository.findByAccountBookAndName(income.getAccountBook(), incomeCategory.getCategory()).orElseThrow(CategoryNotFoundException::new);
        String updateCategory = income.updateCategory(category);

        return UpdateCategoryResult.builder()
                .category(updateCategory)
                .updateItemId(income.getId())
                .build();
    }

    @Transactional
    public AddIncomeResult addIncome(AddIncomeRequest request) {
        return null;
    }

    @Transactional
    public AddRecordResult addRecord(AddRecordRequest request) {
        return null;
    }



}
