package com.uliga.uliga_backend.domain.Budget.application;

import com.uliga.uliga_backend.domain.AccountBook.dao.AccountBookRepository;
import com.uliga.uliga_backend.domain.AccountBook.exception.CategoryNotFoundException;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Budget.dao.BudgetRepository;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Budget.model.Budget;
import com.uliga.uliga_backend.domain.Category.dao.CategoryRepository;
import com.uliga.uliga_backend.domain.Category.model.Category;
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

    @Transactional
    public BudgetInfoQ addBudgetToAccountBook(CreateBudgetDto dto) {
        AccountBook accountBook = accountBookRepository.findById(dto.getId()).orElseThrow(NotFoundByIdException::new);
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
}
