package com.uliga.uliga_backend.domain.budget.application;

import static com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.*;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Category.repository.CategoryRepository;
import com.uliga.uliga_backend.domain.Record.repository.RecordRepository;
import com.uliga.uliga_backend.domain.account_book.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.account_book.model.AccountBook;
import com.uliga.uliga_backend.domain.account_book.repository.AccountBookRepository;
import com.uliga.uliga_backend.domain.account_book_data.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.budget.dto.BudgetDTO.BudgetUpdateRequest;
import com.uliga.uliga_backend.domain.budget.dto.BudgetDTO.BudgetUpdateRequest.BudgetCompare;
import com.uliga.uliga_backend.domain.budget.dto.BudgetDTO.CreateBudgetDto;
import com.uliga.uliga_backend.domain.budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.budget.exception.BudgetNotExistsException;
import com.uliga.uliga_backend.domain.budget.model.Budget;
import com.uliga.uliga_backend.domain.budget.repository.BudgetRepository;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.InvalidDataValueException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountBookRepository accountBookRepository;
    private final CategoryRepository categoryRepository;
    private final RecordRepository recordRepository;
    private final ObjectMapper mapper;

    /**
     * 한달 가계부 예산 총합 조회
     *
     * @param accountBookId 가계부 아이디
     * @param year          년도
     * @param month         달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public MonthlySumQ getMonthlyBudgetSum(Long accountBookId, Long year, Long month) {
        return budgetRepository.getMonthlySumByAccountBookId(accountBookId, year, month).orElse(new MonthlySumQ(0L));
    }

    /**
     * 가계부 예산 등록
     *
     * @param createBudgetMap 파라미터로 값이 넘어오는 map
     * @return 예산 등록 결과
     */
    @Transactional
                
    public BudgetInfoQ addBudget(Map<String, Object> createBudgetMap) {
                
        CreateBudgetDto createBudgetDto = mapper.convertValue(createBudgetMap, CreateBudgetDto.class);
        AccountBook accountBook = accountBookRepository.findById(createBudgetDto.getId()).orElseThrow(() -> new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        Optional<Budget> budgetByAccountBookIdAndYearAndMonth = budgetRepository.findByAccountBookIdAndYearAndMonth(createBudgetDto.getId(), createBudgetDto.getYear(), createBudgetDto.getMonth());
        if (budgetByAccountBookIdAndYearAndMonth.isPre
                        sent()) {
                        
            Budget budget = budgetByAccountBookIdAndYearAndMonth.get();
            if (createBudgetDto.getCategory() != null) {
                Category category = categoryRepository.findByAccountBookAndName(accountBook, createBudgetDto.getCategory()).orElseThrow(CategoryNotFoundException::new);
                budget.updateValue(createBudgetDto.getValue());
                budget.updateCategory(category);
                return budget.toInfoQ();
            } else {
                budget.updateValue(createBudgetDto.getValue());
                return budget.toInfoQ();
            }
                        
                        
        } else {
            if (createBudgetDto.getCategory() != null) {
                Category category = categoryRepository.findByAccountBookAndName(accountBook, createBudgetDto.getCategory()).orElseThrow(CategoryNotFoundException::new);
                Budget build = Budget.builder()
                        .year(createBudgetDto.getYear())
                        .month(createBudgetDto.getMonth())
                        .accountBook(accountBook)
                        .value(createBudgetDto.getValue())
                        .category(category).build();
                budgetRepository.save(build);
                return build.toInfoQ();
            } else {
                Budget build = Budget.builder()
                        .year(createBudgetDto.getYear())
                        .month(createBudgetDto.getMonth())
                        .value(createBudgetDto.getValue())
                        .accountBook(accountBook).build();
         

    


    }

    /**
     * 가계부 분석 - 예산과 비교
     *
     * @param accountBookId 가계부 아이디
     * @param year          년도
     * @param month         달
     * @return 비교 결과
     */
    @Transactional(readOnly = true)
    public BudgetCompare compareWithBudget(Long accountBookId, Long year, Long month) {
        Optional<MonthlySumQ> recordSum = recordRepository.getMonthlySumByAccountBookId(accountBookId, year, month);
        Optional<MonthlySumQ> budgetSum = budgetRepository.getMonthlySumByAccountBookId(accountBookId, year, month);
        if (recordSum.isPresent() && budgetSum.isPresent()) {
            MonthlySumQ record = recordSum.get();
            MonthlySumQ budget = budgetSum.get();
            return new BudgetCompare(budget.getValue(), record.getValue(), budget.getValue() - record.getValue());
        } else if (budgetSum.isPresent()) {
            MonthlySumQ budget = budgetSum.get();
            return new BudgetCompare(budget.getValue(), 0L, budget.getValue());
        } else if (recordSum.isPresent()) {
            MonthlySumQ record = recordSum.get();
            return new BudgetCompare(0L, record.getValue(), -record.getValue());
        } else {
            return new BudgetCompare(0L, 0L, 0L);
        }
    }

    /**
     * 예산 정보 업데이트
     *
     * @param updates 업데이트할 항목들 map
     * @return 업데이트 결과
     */
    @Transactional
    public BudgetUpdateRequest updateBudget(Map<String, Object> updates) {
        BudgetUpdateRequest updateRequest = mapper.convertValue(updates, BudgetUpdateRequest.class);
        if (updateRequest.getId() == null) {
            throw new IdNotFoundException("가계부 아이디 값이 넘어오지 않았습니다");
        }
        if (updateRequest.getYear() == null || updateRequest.getMonth() == null) {
                
            throw new InvalidDataValueException("업데이트 하려는 예산의 년도 혹은 달 값이 들어오지 않았습니다");
        }
        Budget budget = budgetRepository.findByAccountBookIdAndYearAndMonth(updateRequest.getId(), updateRequest.getYear(), updateRequest.getMonth()).orElseThrow(BudgetNotExistsException::new);
        if (updateRequest.getValue() != null) {
            budget.updateValue(updateRequest.getVa
                    lue());
                    
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookIdAndName(updateRequest.getId(), updateRequest.getCategory()).orElseThrow(CategoryNotFoundException::new);
            budget.updateCategory(category);
        }

        return updateRequest;
    }
}
