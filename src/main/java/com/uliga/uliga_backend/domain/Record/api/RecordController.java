package com.uliga.uliga_backend.domain.Record.api;

import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
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

import static com.uliga.uliga_backend.domain.Record.dto.RecordDTO.*;

@Tag(name = "지출", description = "지출 관련 API 입니다")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "지출 업데이트 API", description = "지출 업데이트 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지출 업데이트시", content = @Content(schema = @Schema(implementation = RecordUpdateRequest.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "")
    public ResponseEntity<RecordUpdateRequest> updateRecord(@RequestBody Map<String, Object> updates) {
        log.info("지출 업데이트 API 호출");
        return ResponseEntity.ok(recordService.updateRecord(updates));
    }

    @Operation(summary = "멤버 지출 전체 조회 API", description = "멤버 지출 전체 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RecordInfoQ.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Page<RecordInfoQ>> getMemberRecords(@PathVariable("id") Long id, Pageable pageable) {
        log.info("멤버 지출 전체 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(recordService.getMemberRecords(currentMemberId, id, pageable));
    }

    @Operation(summary = "멤버 지출 카테고리 별 전체 조회 API", description = "멤버 지출 카테고리별 전체 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RecordInfoQ.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/{category}")
    public ResponseEntity<Page<RecordInfoQ>> getMemberRecordsByCategory(@PathVariable("id") Long id, @PathVariable("category") String category, Pageable pageable) {
        log.info("멤버 지출 카테고리 별 전체 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(recordService.getMemberRecordsByCategory(currentMemberId, id, category, pageable));
    }
}
