package com.uliga.uliga_backend.domain.Income.api;

import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "수입", description = "수입 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

    @PatchMapping("")
    public ResponseEntity<IncomeDTO.IncomeUpdateRequest> updateIncome(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(incomeService.updateIncome(updates));
    }
}
