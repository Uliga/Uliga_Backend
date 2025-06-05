package com.uliga.uliga_backend.domain.budget.api;

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

import com.uliga.uliga_backend.domain.budget.application.BudgetServiceV2;
import com.uliga.uliga_backend.domain.budget.dto.BudgetDTO.CreateBudgetDto;
import com.uliga.uliga_backend.domain.budget.dto.req.BudgetQueryDto;
import com.uliga.uliga_backend.domain.budget.dto.req.UpdateBudgetDto;
import com.uliga.uliga_backend.domain.budget.dto.res.BudgetDto;
import com.uliga.uliga_backend.global.common.annotation.Serialize;
import com.uliga.uliga_backend.jooq.tables.pojos.Budget;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "예산 API")
@RestController
@RequestMapping("v2/budget")
@RequiredArgsConstructor
public class BudgetControllerV2 {
  private final BudgetServiceV2 budgetService;

  @Operation(summary = "예산 조회 API")
  @GetMapping()
  @Serialize(dto = BudgetDto.class)
  public ResponseEntity<Flux<Budget>> getBudgets(@ModelAttribute BudgetQueryDto query) {
    return ResponseEntity.ok(budgetService.getBudgets(query));
  }

  @Operation(summary = "예산 추가 API")
  @PostMapping()
  @Serialize(dto = BudgetDto.class)
  public ResponseEntity<Mono<Budget>> createBudget(@RequestBody CreateBudgetDto dto) {
    return ResponseEntity.ok(budgetService.createBudget(dto));
  }

  @Operation(summary = "예산 업데이트 API")
  @PatchMapping()
  @Serialize(dto = BudgetDto.class)
  public ResponseEntity<Mono<Budget>> updateBudget(@RequestBody UpdateBudgetDto dto) {
    return ResponseEntity.ok(budgetService.updateBudget(dto));
  }

  @Operation(summary = "예산 삭제 API")
  @DeleteMapping("{budgetId}")
  @Serialize(dto = BudgetDto.class)
  public ResponseEntity<Mono<Budget>> deleteBudget(@PathVariable("budgetId") Long budgetId) {
    return ResponseEntity.ok(budgetService.deleteBudget(budgetId));
  }
}
