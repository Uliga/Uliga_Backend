package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.expense_category.req.CreateExpenseCategoryDto;
import com.uliga.uliga_backend.dto.expense_category.req.ExpenseCategoryQuery;
import com.uliga.uliga_backend.dto.expense_category.req.UpdateExpenseCategoryDto;
import com.uliga.uliga_backend.jooq.tables.pojos.ExpenseCategory;
import com.uliga.uliga_backend.repository.ExpenseCategoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
  private final ExpenseCategoryRepository expenseCategoryRepository;

  public Flux<ExpenseCategory> getExpenseCategories(ExpenseCategoryQuery query) {
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
