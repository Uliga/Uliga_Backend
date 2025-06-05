package com.uliga.uliga_backend.domain.expense.application;

package com.uliga.uliga_backend.domain.expense.application;

import org.springframework.stereotype.Service;
import com.uliga.uliga_backend.domain.expense.dto.req.CreateExpenseDto;
import com.uliga.uliga_backend.domain.expense.dto.req.ExpenseQueryDto;
import com.uliga.uliga_backend.domain.expense.dto.req.UpdateExpenseDto;
import com.uliga.uliga_backend.domain.expense.repository.ExpenseRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.Expense;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExpenseService {
  private final ExpenseRepository expenseRepository;

  public Flux<Expense> getExpenses(ExpenseQueryDto query) {
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
}
