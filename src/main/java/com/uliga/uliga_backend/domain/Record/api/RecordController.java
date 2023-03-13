package com.uliga.uliga_backend.domain.Record.api;

import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
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
            @ApiResponse(responseCode = "200", description = "지출 업데이트시", content = @Content(schema = @Schema(implementation = RecordUpdateRequest.class)))
    })
    @PatchMapping(value = "")
    public ResponseEntity<RecordUpdateRequest> updateRecord(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(recordService.updateRecord(updates));
    }

    @Operation(summary = "멤버 지출 전체 조회 API", description = "멤버 지출 전체 조회 API 입니다")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Page<RecordInfoQ>> getMemberRecords(@PathVariable("id") Long id, Pageable pageable) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(recordService.getMemberRecords(currentMemberId, id, pageable));
    }

}
