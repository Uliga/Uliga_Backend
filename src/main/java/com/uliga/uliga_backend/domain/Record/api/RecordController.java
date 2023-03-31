package com.uliga.uliga_backend.domain.Record.api;

import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.RecordComment.dto.NativeQ.RecordCommentInfoQ;
import com.uliga.uliga_backend.domain.RecordComment.dto.RecordCommentDto.RecordCommentCreateDto;
import com.uliga.uliga_backend.global.error.response.ErrorResponse;
import com.uliga.uliga_backend.global.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import static io.swagger.v3.oas.annotations.enums.ParameterIn.*;

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
    @GetMapping(value = "")
    public ResponseEntity<Page<RecordInfoQ>> getMemberRecords(Pageable pageable) {
        log.info("멤버 지출 전체 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(recordService.getMemberRecords(currentMemberId, pageable));

    }

    @Operation(summary = "지출 상세 내역 조회", description = "지출 상세 내역 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RecordInfoDetail.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<RecordInfoDetail> getRecordInfoDetail(@Parameter(name = "id", description = "지출 아이디", in = PATH) @PathVariable("id") Long id) {
        log.info("지출 상세 내력 조회 API 호출");
        return ResponseEntity.ok(recordService.getRecordInfoDetail(id));
    }

    @Operation(summary = "멤버 가계부 별 지출 전체/년도별/월별 조회 API", description = "멤버 가계부 별 지출 전체/년도별/월별 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = RecordInfoQ.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/accountBook/{id}")
    public ResponseEntity<Page<RecordInfoQ>> getMemberRecordsByAccountBook(@Parameter(name = "id", description = "가계부 아이디", in = PATH) @PathVariable("id") Long id,
                                                                           @RequestParam(name = "categoryId", required = false) Long categoryId,
                                                                           @RequestParam(name = "year", required = false) Long year,
                                                                           @RequestParam(name = "month", required = false) Long month, Pageable pageable) {
        log.info("멤버 가계부별 지출 전체 조회 API 호출");
        return ResponseEntity.ok(recordService.getMemberRecordsByAccountBook(id, categoryId, year, month, pageable));
    }


    @Operation(summary = "지출에 댓글 추가 API", description = "지출에 댓글 추가하는 API 입니다")
    @PostMapping(value = "/{id}/comment")
    public ResponseEntity<RecordCommentInfoQ> addCommentToRecord(@Parameter(name = "id", description = "지출 아이디", in = PATH) @PathVariable("id") Long id, @RequestBody RecordCommentCreateDto createDto) {
        return ResponseEntity.ok(recordService.addCommentToRecord(id, createDto));
    }

    @Operation(summary = "지출 삭제 API", description = "지출 삭제 API 입니다")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@Parameter(name = "id", description = "지출 아이디", in = PATH) @PathVariable("id") Long id) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        recordService.deleteRecord(currentMemberId, id);
        return ResponseEntity.ok("DELETED");
    }
}
