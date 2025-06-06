package com.uliga.uliga_backend.domain.fixed_revenue.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixedRevenueQueryDto {
  @NotNull
  private Long accountBookId;
}