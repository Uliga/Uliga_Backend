package com.uliga.uliga_backend.global.common.dto.req;

import com.uliga.uliga_backend.global.common.dto.OrderDirection;
import com.uliga.uliga_backend.global.common.dto.OrderField;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderByQuery {
  public OrderField field;
  public OrderDirection direction;
}
