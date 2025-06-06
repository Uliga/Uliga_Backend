package com.uliga.uliga_backend.domain.revenue.dto.req;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RevenueQueryDto {
  private LocalDate date;
  private Long accountBookId;
  private Long userId;
  private LocalDate from;
  private LocalDate to;
}