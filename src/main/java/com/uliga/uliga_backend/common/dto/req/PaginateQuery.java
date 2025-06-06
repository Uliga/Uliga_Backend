package com.uliga.uliga_backend.common.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginateQuery {

  private Integer page = 1;
  private Integer pageSize = 10;
}