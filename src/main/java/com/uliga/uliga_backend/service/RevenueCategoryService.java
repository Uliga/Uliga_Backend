package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.revenue_category.req.CreateRevenueCategoryDto;
import com.uliga.uliga_backend.dto.revenue_category.req.RevenueCategoryQuery;
import com.uliga.uliga_backend.dto.revenue_category.req.UpdateRevenueCategoryDto;
import com.uliga.uliga_backend.entity.RevenueCategoryEntity;
import com.uliga.uliga_backend.repository.RevenueCategoryRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevenueCategoryService {
  private final RevenueCategoryRepository revenueCategoryRepository;

  public Flux<RevenueCategoryEntity> getExpenseCategories(RevenueCategoryQuery query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getExpenseCategories'");
  }

  public Mono<RevenueCategoryEntity> createExpenseCategory(CreateRevenueCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createExpenseCategory'");
  }

  public Mono<RevenueCategoryEntity> updateExpenseCategory(UpdateRevenueCategoryDto dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateExpenseCategory'");
  }

  public Mono<RevenueCategoryEntity> deleteExpenseCategory(Long expenseCategoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteExpenseCategory'");
  }
}
