package com.uliga.uliga_backend.domain.AccountBookData.application;

import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlyCompareQ;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataMapper;
import com.uliga.uliga_backend.domain.AccountBookData.dao.AccountBookDataRepository;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.*;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import com.uliga.uliga_backend.domain.Income.dao.IncomeRepository;
import com.uliga.uliga_backend.domain.Record.dao.RecordRepository;
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
     * @param id    생성자의 아이디
     * @param items 아이템 생성 요청
     * @return 아이템 생성 결과
     */
    @Transactional
    public CreateResult createItems(Long id, AccountBookDataDTO.CreateItems items) {
        return null;
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
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @param pageable 페이징 정보
     * @param category 카테고리
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
