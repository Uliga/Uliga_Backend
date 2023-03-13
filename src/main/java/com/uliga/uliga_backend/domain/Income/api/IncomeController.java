package com.uliga.uliga_backend.domain.Income.api;

import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dto.IncomeDTO;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @PatchMapping(value = "")
    public ResponseEntity<IncomeUpdateRequest> updateIncome(@RequestBody Map<String, Object> updates) {
        log.info("수입 업데이트 API 호출");
        return ResponseEntity.ok(incomeService.updateIncome(updates));
    }

    @Operation(summary = "멤버 수입 전체 조회 API", description = "멤버 수입 전체 조회 API 입니다")
    @GetMapping("/{id}")
    public ResponseEntity<Page<IncomeInfoQ>> getMemberIncomes(@PathVariable("id") Long id, Pageable pageable) {
        log.info("멤버 수입 전체 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(incomeService.getMemberIncomes(currentMemberId, id, pageable));
    }

    @Operation(summary = "멤버 수입 카테고리별 전체 조회 API", description = "멤버 수입 카테고리 별 전체 조회 API 입니다")
    @GetMapping(value = "/{id}/{category}")
    public ResponseEntity<Page<IncomeInfoQ>> getMemberIncomesByCategory(@PathVariable("id") Long id, @PathVariable("category") String category, Pageable pageable) {
        log.info("멤버 수입 카테고리별 전체 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(incomeService.getMemberIncomesByCategory(currentMemberId, id, category, pageable));
    }
}
