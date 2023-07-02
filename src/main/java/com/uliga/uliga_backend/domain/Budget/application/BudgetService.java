package com.uliga.uliga_backend.domain.Budget.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.AccountBook.exception.BudgetAlreadyExists;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Budget.exception.BudgetNotExistsException;
import com.uliga.uliga_backend.domain.Budget.model.Budget;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.global.error.exception.IdNotFoundException;
import com.uliga.uliga_backend.global.error.exception.InvalidDataValueException;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import org.springframework.transaction.annotation.Transactional;import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final AccountBookRepository accountBookRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper mapper;

    /**
     * 한달 가계부 예산 총합 조회
     * @param accountBookId 가계부 아이디
     * @param year 년도
     * @param month 달
     * @return 조회 결과
     */
    @Transactional(readOnly = true)
    public MonthlySumQ getMonthlyBudgetSum(Long accountBookId, Long year, Long month) {
        return budgetRepository.getMonthlySumByAccountBookId(accountBookId, year, month).orElse(new MonthlySumQ(0L));
    }

    /**
     * 가계부에 예산 추가
     * @param dto 예산 정보
     * @return 생성된 예산 정보
     */
    @Transactional
    public BudgetInfoQ addBudgetToAccountBook(CreateBudgetDto dto) {
        AccountBook accountBook = accountBookRepository.findById(dto.getId()).orElseThrow(()->new NotFoundByIdException("해당 아이디로 존재하는 가계부가 없습니다"));
        if (budgetRepository.existsBudgetByAccountBookIdAndYearAndMonth(dto.getId(), dto.getYear(), dto.getMonth())) {
            throw new BudgetAlreadyExists();
        }
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookAndName(accountBook, dto.getCategory()).orElseThrow(CategoryNotFoundException::new);
            Budget build = Budget.builder()
                    .year(dto.getYear())
                    .month(dto.getMonth())
                    .accountBook(accountBook)
                    .value(dto.getValue())
                    .category(category).build();
            budgetRepository.save(build);
            return build.toInfoQ();
        } else {
            Budget build = Budget.builder()
                    .year(dto.getYear())
                    .month(dto.getMonth())
                    .value(dto.getValue())
                    .accountBook(accountBook).build();
            budgetRepository.save(build);
            return build.toInfoQ();
        }
    }

    /**
     * 예산 정보 업데이트
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
            budget.updateValue(updateRequest.getValue());
        }
        if (updateRequest.getCategory() != null) {
            Category category = categoryRepository.findByAccountBookIdAndName(updateRequest.getId(), updateRequest.getCategory()).orElseThrow(CategoryNotFoundException::new);
            budget.updateCategory(category);
        }

        return updateRequest;
    }
}
