package com.uliga.uliga_backend.fixed_revenue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uliga.uliga_backend.common.annotation.Serialize;
import com.uliga.uliga_backend.domain.fixed_revenue.application.FixedRevenueService;
import com.uliga.uliga_backend.domain.fixed_revenue.dto.req.CreateFixedRevenueDto;
import com.uliga.uliga_backend.domain.fixed_revenue.dto.req.FixedRevenueQueryDto;
import com.uliga.uliga_backend.domain.fixed_revenue.dto.req.UpdateFixedRevenueDto;
import com.uliga.uliga_backend.domain.fixed_revenue.dto.res.FixedRevenueDto;
import com.uliga.uliga_backend.jooq.tables.pojos.FixedRevenue;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "고정 수익 API")
@RestController
@RequestMapping("v2/fixed-revenue")
@RequiredArgsConstructor
public class FixedRevenueController {
  private final FixedRevenueService fixedRevenueService;

  @Operation(summary = "고정 수익 조회 API")
  @GetMapping()
  @Serialize(dto = FixedRevenueDto.class)
  public ResponseEntity<Flux<FixedRevenue>> getFixedRevenues(@ModelAttribute FixedRevenueQueryDto query) {
    return ResponseEntity.ok(fixedRevenueService.getFixedRevenues(query));
  }

  @Operation(summary = "고정 수익 생성 API")
  @PostMapping()
  @Serialize(dto = FixedRevenueDto.class)
  public ResponseEntity<Mono<FixedRevenue>> createFixedRevenue(@RequestBody CreateFixedRevenueDto dto) {
    return ResponseEntity.ok(fixedRevenueService.createFixedRevenue(dto));
  }

  @Operation(summary = "고정 수익 수정 API")
  @PatchMapping()
  @Serialize(dto = FixedRevenueDto.class)
  public ResponseEntity<Mono<FixedRevenue>> updateFixedRevenue(@RequestBody UpdateFixedRevenueDto dto) {
    return ResponseEntity.ok(fixedRevenueService.updateFixedRevenue(dto));
  }

  @Operation(summary = "고정 수익 삭제 API")
  @DeleteMapping("/{fixedRevenueId}")
  @Serialize(dto = FixedRevenueDto.class)
  public ResponseEntity<Mono<FixedRevenue>> deleteFixedRevenue(@PathVariable("fixedRevenueId") Long fixedRevenueId) {
    return ResponseEntity.ok(fixedRevenueService.deleteFixedRevenue(fixedRevenueId));
  }
}