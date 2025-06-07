package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.budget.req.BudgetQueryDto;
import com.uliga.uliga_backend.dto.budget.req.CreateBudgetDto;
import com.uliga.uliga_backend.dto.budget.req.UpdateBudgetDto;
import com.uliga.uliga_backend.jooq.tables.pojos.Budget;
import com.uliga.uliga_backend.repository.BudgetRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BudgetServiceV2 {
  private final BudgetRepository budgetRepository;

  public Mono<Budget> createBudget(CreateBudgetDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createBudget'");
  }

  public Mono<Budget> updateBudget(UpdateBudgetDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateBudget'");
  }

  public Mono<Budget> deleteBudget(Long budgetId) {
    throw new UnsupportedOperationException();
  }

  public Flux<Budget> getBudgets(BudgetQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getBudgets'");
  }
}
