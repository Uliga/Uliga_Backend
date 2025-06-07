package com.uliga.uliga_backend.service;

import org.springframework.stereotype.Service;

import com.uliga.uliga_backend.dto.fixed_revenue.req.CreateFixedRevenueDto;
import com.uliga.uliga_backend.dto.fixed_revenue.req.FixedRevenueQueryDto;
import com.uliga.uliga_backend.dto.fixed_revenue.req.UpdateFixedRevenueDto;
import com.uliga.uliga_backend.jooq.tables.pojos.FixedRevenue;
import com.uliga.uliga_backend.repository.FixedRevenueRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FixedRevenueService {
  private final FixedRevenueRepository fixedRevenueRepository;

  public Flux<FixedRevenue> getFixedRevenues(FixedRevenueQueryDto query) {
    throw new UnsupportedOperationException("Unimplemented method 'getFixedRevenues'");
  }

  public Mono<FixedRevenue> createFixedRevenue(CreateFixedRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createFixedRevenue'");
  }

  public Mono<FixedRevenue> updateFixedRevenue(UpdateFixedRevenueDto dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateFixedRevenue'");
  }

  public Mono<FixedRevenue> deleteFixedRevenue(Long fixedRevenueId) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteFixedRevenue'");
  }
}