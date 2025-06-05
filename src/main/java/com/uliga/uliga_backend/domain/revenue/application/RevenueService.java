package com.uliga.uliga_backend.domain.revenue.application;

import org.springframework.stereotype.Service;
import com.uliga.uliga_backend.domain.revenue.dto.req.CreateRevenueDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.RevenueQueryDto;
import com.uliga.uliga_backend.domain.revenue.dto.req.UpdateRevenueDto;
import com.uliga.uliga_backend.domain.revenue.repository.RevenueRepository;
import com.uliga.uliga_backend.jooq.tables.pojos.Revenue;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RevenueService {
  private final RevenueRepository revenueRepository;

  public Flux<Revenue> getRevenues(RevenueQueryDto query) {
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
}