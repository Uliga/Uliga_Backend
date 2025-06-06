package com.uliga.uliga_backend.expense.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import com.uliga.uliga_backend.common.annotation.SerializePaginated;
import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.expense.dto.req.CreateExpenseDto;
import com.uliga.uliga_backend.expense.dto.req.ExpenseQueryDto;
import com.uliga.uliga_backend.expense.dto.req.ExpenseSumQueryDto;
import com.uliga.uliga_backend.expense.dto.req.UpdateExpenseDto;
import com.uliga.uliga_backend.expense.dto.res.ExpenseDto;
import com.uliga.uliga_backend.expense.dto.res.ExpenseSumDto;
import com.uliga.uliga_backend.expense.service.ExpenseService;
import com.uliga.uliga_backend.jooq.tables.pojos.Expense;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "지출 API")
@RestController
@RequestMapping("v2/expense")
@RequiredArgsConstructor
public class ExpenseController {
  private final ExpenseService expenseService;

  @Operation(summary = "지출 조회 API")
  @GetMapping()
  @SerializePaginated(dto = ExpenseDto.class)
  public ResponseEntity<Mono<PaginatedDto<Expense>>> getExpenses(@ModelAttribute @Validated ExpenseQueryDto query,
      @ModelAttribute PaginateQuery paginate, @ModelAttribute OrderByQuery orderBy) {
    return ResponseEntity.ok(expenseService.getExpenses(query, paginate, orderBy));
  }

  @Operation(summary = "지출 상세 조회 API")
  @GetMapping("/{expenseId}")
  @Serialize(dto = ExpenseDto.class)
  public ResponseEntity<Mono<Expense>> getExpense(@PathVariable("expenseId") Long expenseId) {
    return ResponseEntity.ok(expenseService.getExpense(expenseId));
  }

  @Operation(summary = "기간 별 지출 합 조회 API")
  @GetMapping("/sum")
  @Serialize(dto = ExpenseSumDto.class)
  public ResponseEntity<Flux<ExpenseSumDto>> getExpenseSums(@ModelAttribute ExpenseSumQueryDto query) {
    return ResponseEntity.ok(expenseService.getExpenseSums(query));
  }

  @Operation(summary = "지출 생성 API")
  @PostMapping()
  @Serialize(dto = ExpenseDto.class)
  public ResponseEntity<Mono<Expense>> createExpense(@RequestBody CreateExpenseDto dto) {
    return ResponseEntity.ok(expenseService.createExpense(dto));
  }

  @Operation(summary = "지출 수정 API")
  @PatchMapping()
  @Serialize(dto = ExpenseDto.class)
  public ResponseEntity<Mono<Expense>> updateExpense(@RequestBody UpdateExpenseDto dto) {
    return ResponseEntity.ok(expenseService.updateExpense(dto));
  }

  @Operation(summary = "지출 삭제 API")
  @DeleteMapping("/{expenseId}")
  @Serialize(dto = ExpenseDto.class)
  public ResponseEntity<Mono<Expense>> deleteExpense(@PathVariable("expenseId") Long expenseId) {
    return ResponseEntity.ok(expenseService.deleteExpense(expenseId));
  }

}
