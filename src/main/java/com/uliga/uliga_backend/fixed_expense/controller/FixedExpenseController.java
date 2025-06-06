package com.uliga.uliga_backend.fixed_expense.controller;

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
import com.uliga.uliga_backend.domain.fixed_expense.application.FixedExpenseService;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.CreateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.FixedExpenseQuery;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.FixedExpenseSumQuery;
import com.uliga.uliga_backend.domain.fixed_expense.dto.req.UpdateFixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.res.FixedExpenseDto;
import com.uliga.uliga_backend.domain.fixed_expense.dto.res.FixedExpenseSumDto;
import com.uliga.uliga_backend.jooq.tables.pojos.FixedExpense;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "고정 지출 API")
@RestController
@RequestMapping("v2/fixed-expense")
@RequiredArgsConstructor
public class FixedExpenseController {
  private final FixedExpenseService fixedExpenseService;

  @Operation(summary = "고정 지출 조회 API")
  @GetMapping()
  @Serialize(dto = FixedExpenseDto.class)
  public ResponseEntity<Flux<FixedExpense>> getFixedExpenses(@ModelAttribute FixedExpenseQuery query) {
    return ResponseEntity.ok(fixedExpenseService.getFixedExpenses(query));
  }

  @Operation(summary = "기간 단위 고정 지출 합 조회 API")
  @GetMapping("/sum")
  public ResponseEntity<Mono<FixedExpenseSumDto>> getFixedExpenseSum(@ModelAttribute FixedExpenseSumQuery query) {
    return ResponseEntity.ok(fixedExpenseService.getFixedExpenseSum(query));
  }

  @Operation(summary = "고정 지출 생성 API")
  @PostMapping()
  @Serialize(dto = FixedExpenseDto.class)
  public ResponseEntity<Mono<FixedExpense>> createFixedExpense(@RequestBody CreateFixedExpenseDto dto) {
    return ResponseEntity.ok(fixedExpenseService.createFixedExpense(dto));
  }

  @Operation(summary = "고정 지출 수정 API")
  @PatchMapping()
  @Serialize(dto = FixedExpenseDto.class)
  public ResponseEntity<Mono<FixedExpense>> updateFixedExpense(@RequestBody UpdateFixedExpenseDto dto) {
    return ResponseEntity.ok(fixedExpenseService.updateFixedExpense(dto));
  }

  @Operation(summary = "고정 지출 삭제 API")
  @DeleteMapping("/{fixedExpenseId}")
  @Serialize(dto = FixedExpenseDto.class)
  public ResponseEntity<Mono<FixedExpense>> deleteFixedExpense(@PathVariable("fixedExpenseId") Long fixedExpenseId) {
    return ResponseEntity.ok(fixedExpenseService.deleteFixedExpense(fixedExpenseId));
  }
}