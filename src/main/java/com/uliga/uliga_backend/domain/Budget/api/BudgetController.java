package com.uliga.uliga_backend.domain.Budget.api;

import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @Operation(summary = "예산 업데이트 - 미구현")
    @PatchMapping(value = "")
    public void updateBudget() {
    }
}
