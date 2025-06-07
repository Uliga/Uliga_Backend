package com.uliga.uliga_backend.mapper;

import com.uliga.uliga_backend.entity.AccountBookEntity;
import com.uliga.uliga_backend.jooq.tables.pojos.AccountBook;

public class AccountBookMapper {
  private AccountBookMapper() {
  }

  public static AccountBookEntity toEntity(AccountBook pojo) {
    if (pojo == null) {
      return null;
    }

    return AccountBookEntity.builder()
        .name(pojo.getName())
        .aliasName(pojo.getAliasName())
        .isPrivate(pojo.getIsPrivate())
        .createdAt(pojo.getCreatedAt())
        .updatedAt(pojo.getUpdatedAt())
        .build();
  }

  public static AccountBook toPojo(AccountBookEntity entity) {
    if (entity == null) {
      return null;
    }

    return new AccountBook(entity);
  }
}
