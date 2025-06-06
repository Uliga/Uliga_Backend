package com.uliga.uliga_backend.fixed_expense.dto.req;

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