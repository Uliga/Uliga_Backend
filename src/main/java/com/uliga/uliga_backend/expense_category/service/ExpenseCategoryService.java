package com.uliga.uliga_backend.expense_category.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.domain.expense_category.dto.req.CreateExpenseCategoryDto;
import com.uliga.uliga_backend.domain.expense_category.dto.req.ExpenseCategoryQueryDto;
import com.uliga.uliga_backend.domain.expense_category.dto.req.UpdateExpenseCategoryDto;
import com.uliga.uliga_backend.domain.expense_category.repository.ExpenseCategoryRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.ExpenseCategory;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
  private final ExpenseCategoryRepository expenseCategoryRepository;

  public Flux<ExpenseCategory> getExpenseCategories(ExpenseCategoryQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpenseCategories'");
  }

  public Mono<ExpenseCategory> createExpenseCategory(CreateExpenseCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createExpenseCategory'");
  }

  public Mono<ExpenseCategory> updateExpenseCategory(UpdateExpenseCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateExpenseCategory'");
  }

  public Mono<ExpenseCategory> deleteExpenseCategory(Long expenseCategoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteExpenseCategory'");
  }
}
