package com.uliga.uliga_backend.schedule.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.domain.schedule.application.ScheduleService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "금융 일정", description = "금융 일정 관련 API 입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/schedule")
public class ScheduleController {
  private final ScheduleService scheduleService;

  // @Operation(summary = "멤버 금융일정 조회 API", description = "멤버 금융일정 조회 API 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = GetMemberSchedules.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class)))
  // })
  // @GetMapping(value = "")
  // public ResponseEntity<GetMemberSchedules> getAccountBookSchedules() {

  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // return ResponseEntity.ok(scheduleService.getMemberSchedule(currentMemberId));
  // }

  // @Operation(summary = "금융일정 세부 내용 조회 API", description = "금융 일정 세부 내용 조회 API
  // 입니다")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "조회 성공시", content =
  // @Content(schema = @Schema(implementation = ScheduleDetail.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class)))
  // })
  // @GetMapping(value = "/{id}")
  // public ResponseEntity<ScheduleDetail> getScheduleDetail(
  // @Parameter(name = "id", description = "금융일정 아이디", in = PATH)
  // @PathVariable("id") Long id) {

  // return ResponseEntity.ok(scheduleService.getScheduleDetails(id));
  // }

  // @Operation(summary = "금융 일정 업데이트 API")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "업데이트 성공시", content =
  // @Content(schema = @Schema(implementation =
  // ScheduleDTO.UpdateScheduleRequest.class))),
  // @ApiResponse(responseCode = "401", description = "엑세스 만료시", content =
  // @Content(schema = @Schema(implementation = ErrorResponse.class)))
  // })
  // @PatchMapping(value = "")
  // public ResponseEntity<ScheduleDTO.UpdateScheduleRequest> updateSchedule(
  // @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "금융일정
  // 업데이트 요청", content = @Content(schema = @Schema(implementation =
  // ScheduleDTO.UpdateScheduleRequest.class))) @RequestBody Map<String, Object>
  // updates) {

  // return ResponseEntity.ok(scheduleService.updateSchedule(updates));
  // }

  // @Operation(summary = "금융 일정 삭제 API")
  // @DeleteMapping(value = "/{id}")
  // public ResponseEntity<String> deleteSchedule(
  // @Parameter(name = "id", description = "금융 일정 아이디", in = PATH)
  // @PathVariable("id") Long id) {
  // Long currentMemberId = SecurityUtil.getCurrentMemberId();
  // scheduleService.deleteSchedule(id, currentMemberId);
  // return ResponseEntity.ok("DELETED");
  // }
}
