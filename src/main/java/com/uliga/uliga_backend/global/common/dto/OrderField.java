package com.uliga.uliga_backend.global.common.dto;

import lombok.Getter;

@Getter
public enum OrderField {
  ID("ID"), CREATED_AT("CREATED_AT"),;

  private String name;

  OrderField(String name) {
    this.name = name;
  }
}
