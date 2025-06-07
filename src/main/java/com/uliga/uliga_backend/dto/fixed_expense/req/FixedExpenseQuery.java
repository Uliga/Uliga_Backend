package com.uliga.uliga_backend.dto.fixed_expense.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixedExpenseQuery {
  @NotNull
  private Long accountBookId;
}