package com.uliga.uliga_backend.revenue.dto.req;

import java.time.LocalDate;

import com.uliga.uliga_backend.common.DatePeriod;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RevenueSumQueryDto {
  @NotNull
  private DatePeriod datePeriod;

  @NotNull
  private LocalDate from;

  @NotNull
  private LocalDate to;

  @NotNull
  private Long accountBookId;

  private Long revenueCategoryId;
}
