package com.uliga.uliga_backend.common;

import lombok.Getter;

@Getter
public enum DatePeriod {
  DAILY("date"), WEEKLY("week"), MONTHLY("month"), YEARLY("year");

  private String name;

  DatePeriod(String name) {
    this.name = name;
  }

}
