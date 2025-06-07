package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.fixed_expense.req.CreateFixedExpenseDto;
import com.uliga.uliga_backend.dto.fixed_expense.req.FixedExpenseQuery;
import com.uliga.uliga_backend.dto.fixed_expense.req.FixedExpenseSumQuery;
import com.uliga.uliga_backend.dto.fixed_expense.req.UpdateFixedExpenseDto;
import com.uliga.uliga_backend.dto.fixed_expense.res.FixedExpenseSumDto;
import com.uliga.uliga_backend.entity.FixedExpenseEntity;
import com.uliga.uliga_backend.repository.FixedExpenseRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FixedExpenseService {
  private final FixedExpenseRepository fixedExpenseRepository;

  public Flux<FixedExpenseEntity> getFixedExpenses(FixedExpenseQuery query) {
    throw new UnsupportedOperationException("Unimplemented method 'getFixedExpenses'");
  }

  public Mono<FixedExpenseEntity> createFixedExpense(CreateFixedExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createFixedExpense'");
  }

  public Mono<FixedExpenseEntity> updateFixedExpense(UpdateFixedExpenseDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateFixedExpense'");
  }

  public Mono<FixedExpenseEntity> deleteFixedExpense(Long fixedExpenseId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteFixedExpense'");
  }

  public Mono<FixedExpenseSumDto> getFixedExpenseSum(FixedExpenseSumQuery query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getFixedExpenseSum'");
  }
}