package com.uliga.uliga_backend.domain.budget.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BudgetQueryDto {
  @NotNull
  private Long year;

  @NotNull
  private Long month;

  @NotNull
  private Long accountBookId;

  private Long categoryId;
}
