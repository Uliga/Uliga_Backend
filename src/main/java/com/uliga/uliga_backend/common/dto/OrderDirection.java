package com.uliga.uliga_backend.common.dto;

import lombok.Getter;

@Getter
public enum OrderDirection {
  ASC("ASC"), DESC("DESC");

  private String name;

  OrderDirection(String name) {
    this.name = name;
  }
}
