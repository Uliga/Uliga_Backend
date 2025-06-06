package com.uliga.uliga_backend.domain.expense.dto.req;

import java.time.LocalDate;

import com.uliga.uliga_backend.global.common.DatePeriod;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseSumQueryDto {
  @NotNull
  private DatePeriod datePeriod;

  @NotNull
  private LocalDate from;

  @NotNull
  private LocalDate to;

  @NotNull
  private Long accountBookId;

  private Long expenseCategoryId;
}
