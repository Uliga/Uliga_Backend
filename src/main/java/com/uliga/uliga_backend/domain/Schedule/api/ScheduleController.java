package com.uliga.uliga_backend.domain.Schedule.api;

import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.CreateScheduleRequest;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.GetMemberSchedules;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.ScheduleDeleteRequest;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.ScheduleDetail;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "금융 일정", description = "금융 일정 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "멤버 금융일정 조회 API", description = "멤버 금융일정 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetMemberSchedules.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "")
    public ResponseEntity<GetMemberSchedules> getAccountBookSchedules() {

        log.info("멤버 금융 일정 조회 API 호출");
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(scheduleService.getMemberSchedule(currentMemberId));
    }

    @Operation(summary = "금융일정 세부 내용 조회 API", description = "금융 일정 세부 내용 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ScheduleDetail.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ScheduleDetail> getScheduleDetail(@PathVariable("id") Long id) {

        log.info("금융 일정 세부 내용 조회 API 호출");
        return ResponseEntity.ok(scheduleService.getScheduleDetails(id));
    }

    @Operation(summary = "금융 일정 업데이트 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = ScheduleDTO.UpdateScheduleRequest.class))),
            @ApiResponse(responseCode = "503", description = "엑세스 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping(value = "")
    public ResponseEntity<ScheduleDTO.UpdateScheduleRequest> updateSchedule(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "금융일정 업데이트 요청", content = @Content(schema = @Schema(implementation = ScheduleDTO.UpdateScheduleRequest.class))) @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(scheduleService.updateSchedule(updates));
    }

    @Operation(summary = "금융 일정 삭제 API")
    @DeleteMapping(value = "")
    public ResponseEntity<String> deleteSchedule(@RequestBody ScheduleDeleteRequest deleteRequest) {
        scheduleService.deleteSchedule(deleteRequest);
        return ResponseEntity.ok("DELETED");
    }
}
