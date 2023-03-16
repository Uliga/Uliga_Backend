package com.uliga.uliga_backend.domain.Budget.api;

import com.uliga.uliga_backend.domain.Budget.application.BudgetService;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO;
import com.uliga.uliga_backend.domain.Budget.dto.BudgetDTO.BudgetUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/budget")
public class BudgetController {
    private final BudgetService budgetService;

    @Operation(summary = "예산 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = BudgetUpdateRequest.class)))
    })
    @PatchMapping(value = "")
    public ResponseEntity<BudgetUpdateRequest> updateBudget(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(budgetService.updateBudget(updates));
    }
}
