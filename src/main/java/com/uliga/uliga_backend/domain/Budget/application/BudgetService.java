package com.uliga.uliga_backend.domain.Budget.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public BudgetInfoQ addBudgetToAccountBook(CreateBudgetDto dto) {
        AccountBook accountBook = accountBookRepository.findById(dto.getId()).orElseThrow(NotFoundByIdException::new);
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
