package com.uliga.uliga_backend.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.expense_category.req.CreateExpenseCategoryDto;
import com.uliga.uliga_backend.dto.expense_category.req.ExpenseCategoryQuery;
import com.uliga.uliga_backend.dto.expense_category.req.UpdateExpenseCategoryDto;
import com.uliga.uliga_backend.entity.AccountBookEntity;
import com.uliga.uliga_backend.entity.ExpenseCategoryEntity;
import com.uliga.uliga_backend.repository.ExpenseCategoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class ExpenseCategoryService {
  private final ExpenseCategoryRepository expenseCategoryRepository;

  private final List<String> defaultCategories = Arrays.asList("\uD83C\uDF7D️ 식비", "☕ 카페 ·간식", "\uD83C\uDFE0생활",
      "\uD83C\uDF59편의점,마트,잡화", "\uD83D\uDC55쇼핑", "기타");

  public Mono<Void> createDefaultCategories(AccountBookEntity accountBook) {
    List<ExpenseCategoryEntity> categories = defaultCategories.stream()
        .map(name -> ExpenseCategoryEntity.builder()
            .name(name)
            .accountBookId(accountBook.getId())
            .build())
        .toList();

    // Flux.fromIterable → repository.save(...) 반복 → 완료 후 then()
    return Mono.fromRunnable(() -> {
      expenseCategoryRepository.saveAll(categories);
    })
        .subscribeOn(Schedulers.boundedElastic())
        .then(); // Mono<Void>
  }

  public Flux<ExpenseCategoryEntity> getExpenseCategories(ExpenseCategoryQuery query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpenseCategories'");
  }

  public Mono<ExpenseCategoryEntity> createExpenseCategory(CreateExpenseCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createExpenseCategory'");
  }

  public Mono<ExpenseCategoryEntity> updateExpenseCategory(UpdateExpenseCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateExpenseCategory'");
  }

  public Mono<ExpenseCategoryEntity> deleteExpenseCategory(Long expenseCategoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteExpenseCategory'");
  }
}
