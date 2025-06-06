package com.uliga.uliga_backend.revenue.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.common.dto.req.OrderByQuery;
import com.uliga.uliga_backend.common.dto.req.PaginateQuery;
import com.uliga.uliga_backend.common.dto.res.PaginatedDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.CreateRevenueDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.RevenueQueryDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.RevenueSumQueryDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.UpdateRevenueDto;
import com.uliga.uliga_backend.domain.revenue.dto.res.RevenueSumDto;
import com.uliga.uliga_backend.domain.revenue.repository.RevenueRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.Revenue;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevenueService {
  private final RevenueRepository revenueRepository;

  public Mono<PaginatedDto<Revenue>> getRevenues(RevenueQueryDto query, PaginateQuery paginate, OrderByQuery orderBy) {
    throw new UnsupportedOperationException("Unimplemented method 'getRevenues'");
  }

  public Mono<Revenue> createRevenue(CreateRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createRevenue'");
  }

  public Mono<Revenue> updateRevenue(UpdateRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateRevenue'");
  }

  public Mono<Revenue> deleteRevenue(Long revenueId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteRevenue'");
  }

  public Flux<RevenueSumDto> getRevenueSums(RevenueSumQueryDto query) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRevenueSums'");
  }

  public Mono<Revenue> getRevenue(Long revenueId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRevenue'");
  }
}