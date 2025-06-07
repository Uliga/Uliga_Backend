package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.dto.expense.req.CreateExpenseDto;
import com.uliga.uliga_backend.dto.expense.req.ExpenseQueryDto;
import com.uliga.uliga_backend.dto.expense.req.ExpenseSumQueryDto;
import com.uliga.uliga_backend.dto.expense.req.UpdateExpenseDto;
import com.uliga.uliga_backend.dto.expense.res.ExpenseSumDto;
import com.uliga.uliga_backend.entity.ExpenseEntity;
import com.uliga.uliga_backend.repository.ExpenseRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  private final ExpenseRepository expenseRepository;

  public Mono<PaginatedDto<ExpenseEntity>> getExpenses(ExpenseQueryDto query, PaginateQuery paginate,
      OrderByQuery orderBy) {
    throw new UnsupportedOperationException("Unimplemented method 'getExpenses'");
  }

  public Mono<ExpenseEntity> createExpense(CreateExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createExpense'");
  }

  public Mono<ExpenseEntity> updateExpense(UpdateExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateExpense'");
  }

  public Mono<ExpenseEntity> deleteExpense(Long expenseId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteExpense'");
  }

  public Flux<ExpenseSumDto> getExpenseSums(ExpenseSumQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpenseSums'");
  }

  public Mono<ExpenseEntity> getExpense(Long expenseId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpense'");
  }
}
