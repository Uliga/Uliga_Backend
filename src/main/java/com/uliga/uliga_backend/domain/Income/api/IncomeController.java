package com.uliga.uliga_backend.domain.Income.api;

import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uliga.uliga_backend.domain.Income.dto.IncomeDTO.*;

@Tag(name = "수입", description = "수입 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;
    @Operation(summary = "수입 업데이트 API", description = "수입 업데이트 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수입 업데이트시", content = @Content(schema = @Schema(implementation = IncomeUpdateRequest.class)))
    })
    @PatchMapping("")
    public ResponseEntity<IncomeUpdateRequest> updateIncome(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(incomeService.updateIncome(updates));
    }
}
