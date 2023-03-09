package com.uliga.uliga_backend.domain.Income.api;

import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

    @PatchMapping("/{id}")
    public ResponseEntity<IncomeInfoQ> updateIncome(@PathVariable("id") Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(incomeService.updateIncome(id, updates));
    }
}
