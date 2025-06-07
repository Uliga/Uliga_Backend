package com.uliga.uliga_backend.controller;

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
import com.uliga.uliga_backend.common.annotation.SerializePaginated;
import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.dto.revenue.req.CreateRevenueDto;
import com.uliga.uliga_backend.dto.revenue.req.RevenueQueryDto;
import com.uliga.uliga_backend.dto.revenue.req.RevenueSumQueryDto;
import com.uliga.uliga_backend.dto.revenue.req.UpdateRevenueDto;
import com.uliga.uliga_backend.dto.revenue.res.RevenueDto;
import com.uliga.uliga_backend.dto.revenue.res.RevenueSumDto;
import com.uliga.uliga_backend.entity.RevenueEntity;
import com.uliga.uliga_backend.service.RevenueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "수익 API")
@RestController
@RequestMapping("v2/revenue")
@RequiredArgsConstructor
public class RevenueController {
  private final RevenueService revenueService;

  @Operation(summary = "수익 조회 API")
  @GetMapping()
  @SerializePaginated(dto = RevenueDto.class)
  public ResponseEntity<Mono<PaginatedDto<RevenueEntity>>> getRevenues(@ModelAttribute RevenueQueryDto query,
      @ModelAttribute PaginateQuery paginate, @ModelAttribute OrderByQuery orderBy) {
    return ResponseEntity.ok(revenueService.getRevenues(query, paginate, orderBy));
  }

  @Operation(summary = "수익 상세 조회 API")
  @GetMapping("/{revenueId}")
  @Serialize(dto = RevenueDto.class)
  public ResponseEntity<Mono<RevenueEntity>> getRevenue(@PathVariable("revenueId") Long revenueId) {
    return ResponseEntity.ok(revenueService.getRevenue(revenueId));
  }

  @Operation(summary = "기간 별 수익 총합 조회 API")
  @GetMapping("/sum")
  @Serialize(dto = RevenueSumDto.class)
  public ResponseEntity<Flux<RevenueSumDto>> getRevenueSums(@ModelAttribute RevenueSumQueryDto query) {
    return ResponseEntity.ok(revenueService.getRevenueSums(query));
  }

  @Operation(summary = "수익 생성 API")
  @PostMapping()
  @Serialize(dto = RevenueDto.class)
  public ResponseEntity<Mono<RevenueEntity>> createRevenue(@RequestBody CreateRevenueDto dto) {
    return ResponseEntity.ok(revenueService.createRevenue(dto));
  }

  @Operation(summary = "수익 수정 API")
  @PatchMapping()
  @Serialize(dto = RevenueDto.class)
  public ResponseEntity<Mono<RevenueEntity>> updateRevenue(@RequestBody UpdateRevenueDto dto) {
    return ResponseEntity.ok(revenueService.updateRevenue(dto));
  }

  @Operation(summary = "수익 삭제 API")
  @DeleteMapping("/{revenueId}")
  @Serialize(dto = RevenueDto.class)
  public ResponseEntity<Mono<RevenueEntity>> deleteRevenue(@PathVariable("revenueId") Long revenueId) {
    return ResponseEntity.ok(revenueService.deleteRevenue(revenueId));
  }
}