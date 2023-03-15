package com.uliga.uliga_backend.domain.Schedule.api;

import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.CreateScheduleRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "금융 일정 업데이트 API")
    @PatchMapping(value = "")
    public ResponseEntity<ScheduleDTO.UpdateScheduleRequest> updateSchedule(@RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(scheduleService.updateSchedule(updates));
    }
}
