package com.uliga.uliga_backend.domain.fixed_expense.application;

import org.springframework.stereotype.Service;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.CreateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.FixedExpenseQueryDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.UpdateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.repository.FixedExpenseRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.FixedExpense;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FixedExpenseService {
  private final FixedExpenseRepository fixedExpenseRepository;

  public Flux<FixedExpense> getFixedExpenses(FixedExpenseQueryDto query) {
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
}