package com.uliga.uliga_backend.domain.Record.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.AddRecordRequest;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.AddRecordResult;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO;
import com.uliga.uliga_backend.domain.Category.dto.CategoryDTO.MonthlyRecordSumPerCategories;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dao.RecordMapper;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordInfoDetail;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordUpdateRequest;
import com.uliga.uliga_backend.domain.Record.exception.InvalidRecordDelete;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.RecordComment.dto.NativeQ.RecordCommentInfoQ;
import com.uliga.uliga_backend.domain.RecordComment.dto.RecordCommentDto.RecordCommentCreateDto;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
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
public class RecordService {

    private final RecordRepository recordRepository;
    private final AccountBookRepository accountBookRepository;
    private final RecordMapper mapper;
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    /**
     * 한달 가계부 지출 총합 조회
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public MonthlySumQ getMonthlyRecordSum(Long accountBookId, Long year, Long month) {
        return recordRepository.getMonthlySumByAccountBookId(accountBookId, year, month).orElse(new MonthlySumQ(0L));
    }

    /**
     * 가계부에 지출 추가
     * @param currentMemberId 멤버 아이디
     * @param addRecordRequest 지출 추가 요청
     * @return 지출 추가 결과
     */
    @Transactional
    public AddRecordResult addRecord(Long currentMemberId, AddRecordRequest addRecordRequest) {
        AccountBook accountBook = accountBookRepository.findById(addRecordRequest.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        Category category = categoryRepository.findByAccountBookAndName(accountBook, addRecordRequest.getCategory()).orElseThrow(CategoryNotFoundException::new);
        Member member = memberRepository.findById(currentMemberId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 멤버가 없습니다"));
        String[] split = addRecordRequest.getDate().split("-");
        Date date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();

        Record record = Record.builder()
                .accountBook(accountBook)
                .spend(addRecordRequest.getValue())
                .payment(addRecordRequest.getPayment())
                .category(category)
                .creator(member)
                .memo(addRecordRequest.getMemo())
                .date(date)
                .account(addRecordRequest.getAccount())
                .build();
        recordRepository.save(record);
        List<Long> sharedAccountBookIds = addRecordRequest.getSharedAccountBook();
        List<AccountBook> sharedAccountBooks = accountBookRepository.findAccountBookByAccountBookIds(sharedAccountBookIds);
        List<Category> categories = categoryRepository.findCategoriesByAccountBookIds(sharedAccountBookIds);

        Map<Long, Category> categoryDict = new HashMap<>();
        Map<Long, AccountBook> accountBookDict = new HashMap<>();
        List<Record> toSave = new ArrayList<>();
        for (Category c : categories) {
            categoryDict.put(c.getAccountBook().getId(), c);
        }
        for (AccountBook ab : sharedAccountBooks) {
            accountBookDict.put(ab.getId(), ab);
        }
        for (Long accountBookId : sharedAccountBookIds) {
            Record temp_record = Record.builder()
                    .category(categoryDict.get(accountBookId))
                    .date(date)
                    .accountBook(accountBookDict.get(accountBookId))
                    .memo(addRecordRequest.getMemo())
                    .payment(addRecordRequest.getPayment())
                    .creator(member)
                    .spend(addRecordRequest.getValue())
                    .account(addRecordRequest.getAccount())
                    .build();
            toSave.add(temp_record);
        }
        recordRepository.saveAll(toSave);
        return AddRecordResult.builder()
                .accountBookId(accountBook.getId())
                .recordInfo(record.toInfoQ()).build();

    }

    /**
     * 지출 리스트 다른 가계부에 추가하는 메서드
     * @param records 지출 리스트
     */
    @Transactional
    public void addRecordToOtherAccountBooks(List<Record> records) {
        recordRepository.saveAll(records);
    }

    /**
     * 수입/지출 한개 가계부에 추가
     * @param dto 지출 dto
     * @param accountBook 추가할 가계부
     * @param member 생성한 멤버
     * @param date 지출 날짜
     * @param category 지출 카테고리
     * @return 추가 결과
     */
    @Transactional
    public AccountBookDataDTO.CreateItemResult addItemToAccountBook(AccountBookDataDTO.CreateRecordOrIncomeDto dto, AccountBook accountBook, Member member, Date date, Category category) {
        Record build = Record.builder()
                .payment(dto.getPayment())
                .account(dto.getAccount())
                .creator(member)
                .accountBook(accountBook)
                .spend(dto.getValue())
                .memo(dto.getMemo())
                .date(date)
                .category(category).build();
        recordRepository.save(build);
        return AccountBookDataDTO.CreateItemResult.builder()
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
    }



    /**
     * 지출 정보 업데이트
     * @param updates 업데이트 정보 map
     * @return 업데이트 결과
     */
    @Transactional
    public RecordUpdateRequest updateRecord(Map<String, Object> updates) {
        RecordUpdateRequest patchRecord = objectMapper.convertValue(updates, RecordUpdateRequest.class);
        if (patchRecord.getId() == null) {
            throw new IdNotFoundException("지출 아이디가 null 입니다.");
        }
        Record record = recordRepository.findById(patchRecord.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 지출이 없습니다"));

        if (patchRecord.getAccount() != null) {
            record.updateAccount(patchRecord.getAccount());
        }
        if (patchRecord.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookAndName(record.getAccountBook(), patchRecord.getCategory()).orElseThrow(CategoryNotFoundException::new);
            record.updateCategory(category);
        }
        if (patchRecord.getMemo() != null) {
            record.updateMemo(patchRecord.getMemo());
        }
        if (patchRecord.getValue() != null) {
            record.updateSpend(patchRecord.getValue());
        }
        if (patchRecord.getPayment() != null) {
            record.updatePayment(patchRecord.getPayment());
        }
        if (patchRecord.getDate() != null) {
            record.updateDate(patchRecord.getDate());
        }
        if (patchRecord.getType() != null && patchRecord.getType().equals(AccountBookDataType.INCOME.toString())) {
            record.updateType();
            incomeRepository.createFromAccountBookDataId(record.getId());
            recordRepository.deleteByAccountBookDataIdNativeQ(record.getId());

        }
        return patchRecord;
    }

    /**
     * 가계부 지출 페이징으로 조회
     * @param accountBookId 가계부 아이디
     * @param categoryId 카테고리 아이디
     * @param year 년도
     * @param month 달
     * @param pageable 페이징 정보
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public Page<RecordInfoQ> getMemberRecordsByAccountBook(Long accountBookId, Long categoryId, Long year, Long month, Pageable pageable) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("accountBookId", accountBookId);
        map.put("categoryId", categoryId);
        map.put("year", year);
        map.put("month", month);
        map.put("offset", pageable.getOffset());
        map.put("pageSize", pageable.getPageSize());;
        List<RecordInfoQ> accountBookMemberRecords = mapper.findAccountBookMemberRecords(map);
        List<Long> counted = mapper.countQueryForRecordHistory(map);
        return new PageImpl<>(accountBookMemberRecords, pageable, counted.size());
    }

    /**
     * 멤버 전체 지출 조회
     * @param id 멤버 아이디
     * @param pageable 페이징 정보
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public Page<RecordInfoQ> getMemberRecords(Long id, Pageable pageable) {

        return recordRepository.getMemberRecords(id, pageable);
    }

    /**
     * 지출 삭제
     * @param id 멤버 아이디
     * @param recordId 지출 아이디
     */
    @Transactional
    public void deleteRecord(Long id, Long recordId) {
        Record record = recordRepository.findById(recordId).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 지출이 없습니다"));
        if (record.getCreator().getId().equals(id)) {
            recordRepository.delete(record);
        } else {
            throw new InvalidRecordDelete();
        }

    }

    /**
     * 가계부 분석 -날짜별 지출 총합, 월별 지출 총합 그리고 예산과 비교
     * @param accountBookId 가계부 아이디
     * @param year 조회할 년도
     * @param month 조회할 달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public AccountBookDataDTO.AccountBookDailyRecordSumAndMonthlySum getDailyRecordSumAndMonthlySum(Long accountBookId, Long year, Long month) {
        List<DailyValueQ> monthlyRecord = recordRepository.getDailyRecordSumOfMonth(accountBookId, year, month);

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
        List<MonthlyCompareQ> monthlyCompareInDailyAnalyze = accountBookRepository.getMonthlyCompareInDailyAnalyze(accountBookId, year, month);
        if (monthlyCompareInDailyAnalyze.size() == 2) {
            Long diff = monthlyCompareInDailyAnalyze.get(0).getValue() - monthlyCompareInDailyAnalyze.get(1).getValue();
            return new AccountBookDataDTO.AccountBookDailyRecordSumAndMonthlySum(result, monthlyCompareInDailyAnalyze.get(0).getValue(), diff);
        } else {
            if (monthlyCompareInDailyAnalyze.size() == 0) {
                return new AccountBookDataDTO.AccountBookDailyRecordSumAndMonthlySum(result, 0L, null);
            } else {
                return new AccountBookDataDTO.AccountBookDailyRecordSumAndMonthlySum(result, monthlyCompareInDailyAnalyze.get(0).getValue(), null);
            }

        }


    }

    /**
     * 한달 카테고리 별 지출 총합 조회
     * @param accountBookId 가계부 아이디
     * @param year 조회할 년도
     * @param month 조회할 달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public MonthlyRecordSumPerCategories getMonthlyRecordSumPerCategories(Long accountBookId, Long year, Long month) {
        List<AccountBookCategoryAnalyzeQ> categoryAnalyze = accountBookRepository.findAccountBookCategoryAnalyze(accountBookId, year, month);
        Optional<MonthlySumQ> monthlySum = recordRepository.getMonthlySumByAccountBookId(accountBookId, year, month);
        if (monthlySum.isPresent()) {
            MonthlySumQ monthlySumQ = monthlySum.get();
            Long compare = 0L;
            for (AccountBookCategoryAnalyzeQ analyzeQ : categoryAnalyze) {
                compare += analyzeQ.getValue();
            }
            if (compare.equals(monthlySumQ.getValue())) {
                return new CategoryDTO.MonthlyRecordSumPerCategories(categoryAnalyze, monthlySumQ.getValue());
            } else {
                AccountBookCategoryAnalyzeQ built = new AccountBookCategoryAnalyzeQ(null, "그 외", monthlySumQ.getValue() - compare);
                categoryAnalyze.add(built);
                return new CategoryDTO.MonthlyRecordSumPerCategories(categoryAnalyze, monthlySumQ.getValue());
            }
        } else {
            List<AccountBookCategoryInfoQ> accountBookCategoryInfoById = accountBookRepository.findAccountBookCategoryAnalyze(accountBookId);
            List<AccountBookCategoryAnalyzeQ> result = new ArrayList<>();
            for (AccountBookCategoryInfoQ accountBookCategoryInfoQ : accountBookCategoryInfoById) {
                AccountBookCategoryAnalyzeQ built = new AccountBookCategoryAnalyzeQ(accountBookCategoryInfoQ.getId(), accountBookCategoryInfoQ.getLabel(), 0L);
                result.add(built);

            }
            return new CategoryDTO.MonthlyRecordSumPerCategories(result, 0L);
        }

    }

    /**
     * 지출에 댓글 추가
     * @param id 지출 아이디
     * @param createDto 댓글 정보
     * @return 댓글 추가 결과
     */
    @Transactional
    public RecordCommentInfoQ addCommentToRecord(Long id, RecordCommentCreateDto createDto) {
        return null;
    }

    /**
     * 지출 상세 정보 조회
     * @param id 지출 아이디
     * @return 지출 상세 정보
     */
    @Transactional
    public RecordInfoDetail getRecordInfoDetail(Long id) {
        return null;
    }
}
