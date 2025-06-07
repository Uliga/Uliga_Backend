package com.uliga.uliga_backend.dto.account_book.NativeQ;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class MembersQ {
  private Long count;

  public MembersQ(Long count) {
    this.count = count;
  }
}
