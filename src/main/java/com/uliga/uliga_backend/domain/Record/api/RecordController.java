package com.uliga.uliga_backend.domain.Record.api;

import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.uliga.uliga_backend.domain.Record.dto.RecordDTO.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/record")
public class RecordController {

    private final RecordService recordService;

    @PatchMapping("/{id}")
    public ResponseEntity<RecordInfoQ> updateRecord(@PathVariable("id") Long id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(recordService.updateRecord(id, updates));
    }

}
