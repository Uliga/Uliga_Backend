package com.uliga.uliga_backend.expense.dto.req;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseQueryDto {
  private LocalDate date;
  private Long accountBookId;
  private Long userId;
  private LocalDate from;
  private LocalDate to;
}