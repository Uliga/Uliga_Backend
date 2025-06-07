package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.dto.revenue.req.CreateRevenueDto;
import com.uliga.uliga_backend.dto.revenue.req.RevenueQueryDto;
import com.uliga.uliga_backend.dto.revenue.req.RevenueSumQueryDto;
import com.uliga.uliga_backend.dto.revenue.req.UpdateRevenueDto;
import com.uliga.uliga_backend.dto.revenue.res.RevenueSumDto;
import com.uliga.uliga_backend.entity.RevenueEntity;
import com.uliga.uliga_backend.repository.RevenueRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevenueService {
  private final RevenueRepository revenueRepository;

  public Mono<PaginatedDto<RevenueEntity>> getRevenues(RevenueQueryDto query, PaginateQuery paginate,
      OrderByQuery orderBy) {
    throw new UnsupportedOperationException("Unimplemented method 'getRevenues'");
  }

  public Mono<RevenueEntity> createRevenue(CreateRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createRevenue'");
  }

  public Mono<RevenueEntity> updateRevenue(UpdateRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateRevenue'");
  }

  public Mono<RevenueEntity> deleteRevenue(Long revenueId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteRevenue'");
  }

  public Flux<RevenueSumDto> getRevenueSums(RevenueSumQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRevenueSums'");
  }

  public Mono<RevenueEntity> getRevenue(Long revenueId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRevenue'");
  }
}