package com.uliga.uliga_backend.domain.income.api;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.income.application.IncomeService;
import com.uliga.uliga_backend.domain.income.dto.IncomeDTO.IncomeUpdateRequest;
import com.uliga.uliga_backend.domain.income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.util.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "수입", description = "수입 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

    @Operation(summary = "수입 업데이트 API", description = "수입 업데이트 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수입 업데이트시", content = @Content(schema = @Schema(implementation = IncomeUpdateRequest.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "")
    public ResponseEntity<IncomeUpdateRequest> updateIncome(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "수입 업데이트 요청", content = @Content(schema = @Schema(implementation = IncomeUpdateRequest.class))) @RequestBody Map<String, Object> updates) {

        return ResponseEntity.ok(incomeService.updateIncome(updates));
    }

    @Operation(summary = "멤버 수입 전체 조회 API", description = "멤버 수입 전체 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = IncomeInfoQ.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<Page<IncomeInfoQ>> getMemberIncomes(Pageable pageable) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(incomeService.getMemberIncomes(currentMemberId, pageable));
    }

    @Operation(summary = "멤버 수입 가계부 별 전체/년도별/월회 조회 API", description = "멤버 수입 가계부 별 전체/월별 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = IncomeInfoQ.class))),
            @ApiResponse(responseCode = "401", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/accountBook/{id}")
    public ResponseEntity<Page<IncomeInfoQ>> getMemberIncomesByAccountBook(
            @Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "year", required = false) Long year,
            @RequestParam(name = "month", required = false) Long month, Pageable pageable) {

        return ResponseEntity.ok(incomeService.getMemberIncomesByAccountBook(id, categoryId, year, month, pageable));
    }

    @Operation(summary = "수입 삭제 API", description = "수입 삭제 API 입니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncome(
            @Parameter(name = "id", description = "수입 아이디", in = PATH) @PathVariable("id") Long id) {

        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        incomeService.deleteIncome(currentMemberId, id);
        return ResponseEntity.ok("DELETED");
    }
}
