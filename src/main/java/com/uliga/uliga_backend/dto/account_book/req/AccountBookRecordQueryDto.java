package com.uliga.uliga_backend.dto.account_book.req;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountBookRecordQueryDto {
  @NotNull
  private Long accountBookId;
  private Long categoryId;
  @NotNull
  private LocalDate from;
  @NotNull
  private LocalDate to;
}
