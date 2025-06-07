package com.uliga.uliga_backend.dto.schedule.NativeQ;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleMemberInfoQ {
  private Long id;
  private String username;
  private Long value;

  public ScheduleMemberInfoQ(Long id, String username, Long value) {
    this.id = id;
    this.username = username;
    this.value = value;
  }
}
