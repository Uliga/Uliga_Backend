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
import com.uliga.uliga_backend.dto.expense_category.req.CreateExpenseCategoryDto;
import com.uliga.uliga_backend.dto.expense_category.req.ExpenseCategoryQuery;
import com.uliga.uliga_backend.dto.expense_category.req.UpdateExpenseCategoryDto;
import com.uliga.uliga_backend.dto.expense_category.res.ExpenseCategoryDto;
import com.uliga.uliga_backend.jooq.tables.pojos.ExpenseCategory;
import com.uliga.uliga_backend.service.ExpenseCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "지출 카테고리 API")
@RestController
@RequestMapping("v2/expense-category")
@RequiredArgsConstructor
public class ExpenseCategoryController {
  private final ExpenseCategoryService expenseCategoryService;

  @Operation(summary = "지출 카테고리 조회 API")
  @GetMapping()
  @Serialize(dto = ExpenseCategoryDto.class)
  public ResponseEntity<Flux<ExpenseCategory>> getExpenseCategories(@ModelAttribute ExpenseCategoryQuery query) {
    return ResponseEntity.ok(expenseCategoryService.getExpenseCategories(query));
  }

  @Operation(summary = "지출 카테고리 생성 API")
  @PostMapping()
  @Serialize(dto = ExpenseCategoryDto.class)
  public ResponseEntity<Mono<ExpenseCategory>> createExpenseCategory(@RequestBody CreateExpenseCategoryDto dto) {
    return ResponseEntity.ok(expenseCategoryService.createExpenseCategory(dto));
  }

  @Operation(summary = "지출 카테고리 수정 API")
  @PatchMapping()
  @Serialize(dto = ExpenseCategoryDto.class)
  public ResponseEntity<Mono<ExpenseCategory>> updateExpenseCategory(@RequestBody UpdateExpenseCategoryDto dto) {
    return ResponseEntity.ok(expenseCategoryService.updateExpenseCategory(dto));
  }

  @Operation(summary = "지출 카테고리 삭제 API")
  @DeleteMapping("/{expenseCategoryId}")
  @Serialize(dto = ExpenseCategoryDto.class)
  public ResponseEntity<Mono<ExpenseCategory>> deleteExpenseCategory(
      @PathVariable("expenseCategoryId") Long expenseCategoryId) {
    return ResponseEntity.ok(expenseCategoryService.deleteExpenseCategory(expenseCategoryId));
  }
}
