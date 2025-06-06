package com.uliga.uliga_backend.expense.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.expense.dto.req.CreateExpenseDto;
import com.uliga.uliga_backend.expense.dto.req.ExpenseQueryDto;
import com.uliga.uliga_backend.expense.dto.req.ExpenseSumQueryDto;
import com.uliga.uliga_backend.expense.dto.req.UpdateExpenseDto;
import com.uliga.uliga_backend.expense.dto.res.ExpenseSumDto;
import com.uliga.uliga_backend.expense.repository.ExpenseRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.Expense;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  private final ExpenseRepository expenseRepository;

  public Mono<PaginatedDto<Expense>> getExpenses(ExpenseQueryDto query, PaginateQuery paginate, OrderByQuery orderBy) {
    throw new UnsupportedOperationException("Unimplemented method 'getExpenses'");
  }

  public Mono<Expense> createExpense(CreateExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createExpense'");
  }

  public Mono<Expense> updateExpense(UpdateExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateExpense'");
  }

  public Mono<Expense> deleteExpense(Long expenseId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteExpense'");
  }

  public Flux<ExpenseSumDto> getExpenseSums(ExpenseSumQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpenseSums'");
  }

  public Mono<Expense> getExpense(Long expenseId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpense'");
  }
}
