package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.tables.interfaces.IAccountBook;

import lombok.Getter;

@Getter
public class AccountBookEntity implements IAccountBook {

  private final Long id;
  private final Boolean isPrivate;
  private final String name;
  private final String aliasName;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public AccountBookEntity(IAccountBook value) {
    if (value.getName() == null) {
      throw new IllegalArgumentException("가계부 이름은 필수입니다.");
    }
    if (value.getIsPrivate() == null) {
      throw new IllegalArgumentException("가계부 공개 여부는 필수 입니다.");
    }
    this.id = value.getId();
    this.isPrivate = value.getIsPrivate();
    this.name = value.getName();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.aliasName = value.getAliasName();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .aliasName(aliasName)
        .name(name)
        .isPrivate(isPrivate)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public AccountBookEntity updateName(String name) {
    return toBuilder().name(name).build();
  }

  public AccountBookEntity updateAliasName(String aliasName) {
    return toBuilder().aliasName(aliasName).build();
  }

  public AccountBookEntity updateIsPrivate(Boolean isPrivate) {
    return toBuilder().isPrivate(isPrivate).build();
  }

  @Getter
  public static class Builder implements IAccountBook {
    private Long id;
    private Boolean isPrivate;
    private String name;
    private String aliasName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder isPrivate(Boolean isPrivate) {
      this.isPrivate = isPrivate;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder aliasName(String aliasName) {
      this.aliasName = aliasName;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public AccountBookEntity build() {
      return new AccountBookEntity(this);
    }
  }

}
