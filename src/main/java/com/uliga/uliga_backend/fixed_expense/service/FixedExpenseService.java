package com.uliga.uliga_backend.fixed_expense.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.domain.fixed_expense.dto.req.CreateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.FixedExpenseQuery;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.FixedExpenseSumQuery;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.UpdateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.res.FixedExpenseSumDto;
import com.uliga.uliga_backend.domain.fixed_expense.repository.FixedExpenseRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.FixedExpense;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FixedExpenseService {
  private final FixedExpenseRepository fixedExpenseRepository;

  public Flux<FixedExpense> getFixedExpenses(FixedExpenseQuery query) {
    throw new UnsupportedOperationException("Unimplemented method 'getFixedExpenses'");
  }

  public Mono<FixedExpense> createFixedExpense(CreateFixedExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createFixedExpense'");
  }

  public Mono<FixedExpense> updateFixedExpense(UpdateFixedExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateFixedExpense'");
  }

  public Mono<FixedExpense> deleteFixedExpense(Long fixedExpenseId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteFixedExpense'");
  }

  public Mono<FixedExpenseSumDto> getFixedExpenseSum(FixedExpenseSumQuery query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getFixedExpenseSum'");
  }
}