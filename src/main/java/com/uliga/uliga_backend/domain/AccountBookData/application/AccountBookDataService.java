package com.uliga.uliga_backend.domain.AccountBookData.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlyCompareQ;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataMapper;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataRepository;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.*;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountBookDataService {
    private final AccountBookDataRepository accountBookDataRepository;
    private final IncomeRepository incomeRepository;
    private final RecordRepository recordRepository;
    private final AccountBookDataMapper accountBookDataMapper;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final AccountBookRepository accountBookRepository;

    /**
     * 한달 가계부 수입/지출 조회
     *
     * @param accountBookId 가계부 아이디
     * @param year          조회할 년도
     * @param month         조회할 달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public AccountBookDataDailySum getAccountBookItems(Long accountBookId, Long year, Long month) {

        return AccountBookDataDTO.AccountBookDataDailySum.builder()
                .incomes(incomeRepository.getDailyIncomeSumOfMonth(accountBookId, year, month))
                .records(recordRepository.getDailyRecordSumOfMonth(accountBookId, year, month)).build();
    }

    /**
     * 하루 가계부 수입/지출 내역 상세 조회
     *
     * @param accountBookId 가계부 아이디
     * @param year          년도
     * @param month         달
     * @param day           날짜
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public DailyAccountBookDataDetails getDailyAccountBookDataDetails(Long accountBookId, Long year, Long month, Long day) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("year", year);
        map.put("month", month);
        map.put("day", day);
        return new AccountBookDataDTO.DailyAccountBookDataDetails(accountBookDataMapper.findAccountBookDataOrderByValue(map));
    }

    /**
     * 가계부 아이템 삭제
     *
     * @param deleteItemRequest 가계부 아이템 삭제 요청
     */
    @Transactional
    public void deleteAccountBookData(DeleteItemRequest deleteItemRequest) {
        accountBookDataRepository.deleteAllById(deleteItemRequest.getDeleteIds());
    }


    /**
     * 가계부에 다수의 수입, 지출 한번에 추가
     *
     * @param id          생성자의 아이디
     * @param createItems 아이템 생성 요청
     * @return 아이템 생성 결과
     */
    @Transactional
    public CreateResult createItems(Long id, AccountBookDataDTO.CreateItems createItems) {
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
        // 다른 가계부 아이디와 다른 가계부 객체 매핑
        for (AccountBook ab : accountBooks) {
            otherAccountBooks.put(ab.getId(), ab);
        }
        List<CreateItemResult> createResult = new ArrayList<>();
        List<Record> recordToSave = new ArrayList<>();
        List<Income> incomeToSave = new ArrayList<>();
        for (CreateRecordOrIncomeDto dto : createItems.getCreateRequest()) {
            String[] split = dto.getDate().split("-");
            com.uliga.uliga_backend.domain.Common.Date date = Date.builder()
                    .year(Long.parseLong(split[0]))
                    .month(Long.parseLong(split[1]))
                    .day(Long.parseLong(split[2])).build();
            Category category = categoryDict.get(dto.getCategory());
            if (dto.getIsIncome()) {
                // 수입 생성
                Income toSave = dto.toIncome(accountBook, member, date, category);
                incomeToSave.add(toSave);
                CreateItemResult createItemResult = toSave.toCreateItemResult();
                createResult.add(createItemResult);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = otherAccountBooks.get(accountBookId);
                    Category defaultCategory = defaultCategories.get(accountBookId);

                    incomeToSave.add(Income.builder()
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
                Record record = dto.toRecord(accountBook, member, date, category);
                recordToSave.add(record);
                CreateItemResult createItemResult = record.toCreateItemResult();
                createResult.add(createItemResult);
                for (Long accountBookId : dto.getSharedAccountBook()) {
                    AccountBook sharedAccountBook = otherAccountBooks.get(accountBookId);
                    Category defaultCategory = defaultCategories.get(accountBookId);
                    recordToSave.add(Record.builder()
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

        recordRepository.saveAll(recordToSave);
        incomeRepository.saveAll(incomeToSave);
        return new AccountBookDataDTO.CreateResult(i, r, createResult);
    }

    /**
     * 가계부 내역 조회
     *
     * @param accountBookId 가계부 아이디
     * @param categoryId    카테고리 아이디
     * @param year          년도
     * @param month         월
     * @param pageable      페이징 정보
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
     * 과거 2달과 지출 총액 비교
     *
     * @param accountBookId 가계부 아이디
     * @param year          년도
     * @param month         달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public MonthlyCompare getAccountBookDataMonthlyCompare(Long accountBookId, Long year, Long month) {
        List<MonthlyCompareQ> monthlyCompare = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (int i = 2; i > -1; i--) {
            calendar.set(Math.toIntExact(year), month.intValue() - i, 1);
            Optional<MonthlyCompareQ> monthly = recordRepository.getMonthlyCompare(accountBookId, year, month - i);
            if (monthly.isPresent()) {
                monthlyCompare.add(monthly.get());
            } else {
                MonthlyCompareQ build = MonthlyCompareQ.builder().year((long) calendar.get(Calendar.YEAR)).month((long) calendar.get(Calendar.MONTH)).value(0L).build();
                monthlyCompare.add(build);
            }
        }

        return new AccountBookDataDTO.MonthlyCompare(monthlyCompare);
    }

    /**
     * 가계부 분석 - 내역 조회
     *
     * @param accountBookId 가계부 아이디
     * @param year          년도
     * @param month         달
     * @param pageable      페이징 정보
     * @param category      카테고리
     * @return 내역
     */
    @Transactional(readOnly = true)
    public Page<AccountBookDataQ> getMonthlyAccountBookDetail(Long accountBookId, Long year, Long month, Pageable pageable, String category) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());
        map.put("type", "RECORD");
        if (Objects.equals(category, "그 외")) {
            List<String> extraAccountBookCategory = categoryRepository.findExtraAccountBookCategory(accountBookId, year, month);
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
     * 가계부 분석 - 사용자 지정 날짜 기간동안 가계부 내역 조회
     *
     * @param id       가계부 아이디
     * @param year     년도
     * @param month    달
     * @param startDay 시작일
     * @param endDay   종료일
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
