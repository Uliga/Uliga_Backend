package com.uliga.uliga_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.dto.revenue_category.req.CreateRevenueCategoryDto;
import com.uliga.uliga_backend.dto.revenue_category.req.RevenueCategoryQuery;
import com.uliga.uliga_backend.dto.revenue_category.req.UpdateRevenueCategoryDto;
import com.uliga.uliga_backend.dto.revenue_category.res.RevenueCategoryDto;
import com.uliga.uliga_backend.entity.ExpenseCategoryEntity;
import com.uliga.uliga_backend.entity.RevenueCategoryEntity;
import com.uliga.uliga_backend.service.RevenueCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "수입 카테고리 API")
@RestController
@RequestMapping("v2/revenue-category")
@RequiredArgsConstructor
public class RevenueCategoryController {
  private final RevenueCategoryService revenueCategoryService;

  @Operation(summary = "지출 카테고리 조회 API")
  @GetMapping()
  @Serialize(dto = RevenueCategoryDto.class)
  public ResponseEntity<Flux<RevenueCategoryEntity>> getExpenseCategories(@ModelAttribute RevenueCategoryQuery query) {
    return ResponseEntity.ok(revenueCategoryService.getExpenseCategories(query));
  }

  @Operation(summary = "지출 카테고리 생성 API")
  @PostMapping()
  @Serialize(dto = RevenueCategoryDto.class)
  public ResponseEntity<Mono<RevenueCategoryEntity>> createExpenseCategory(@RequestBody CreateRevenueCategoryDto dto) {
    return ResponseEntity.ok(revenueCategoryService.createExpenseCategory(dto));
  }

  @Operation(summary = "지출 카테고리 수정 API")
  @PatchMapping()
  @Serialize(dto = RevenueCategoryDto.class)
  public ResponseEntity<Mono<RevenueCategoryEntity>> updateExpenseCategory(@RequestBody UpdateRevenueCategoryDto dto) {
    return ResponseEntity.ok(revenueCategoryService.updateExpenseCategory(dto));
  }

  @Operation(summary = "지출 카테고리 삭제 API")
  @DeleteMapping("/{expenseCategoryId}")
  @Serialize(dto = RevenueCategoryDto.class)
  public ResponseEntity<Mono<RevenueCategoryEntity>> deleteExpenseCategory(
      @PathVariable("expenseCategoryId") Long expenseCategoryId) {
    return ResponseEntity.ok(revenueCategoryService.deleteExpenseCategory(expenseCategoryId));
  }
}
