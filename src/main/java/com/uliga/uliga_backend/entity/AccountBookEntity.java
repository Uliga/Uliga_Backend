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

  public AccountBookEntity(IAccountBook pojo) {
    this.id = pojo.getId();
    this.isPrivate = pojo.getIsPrivate();
    this.name = pojo.getName();
    this.createdAt = pojo.getCreatedAt();
    this.updatedAt = pojo.getUpdatedAt();
    this.aliasName = pojo.getAliasName();
  }

  private AccountBookEntity(Builder builder) {
    if (builder.name == null) {
      throw new IllegalArgumentException("가계부 이름은 필수입니다.");
    }
    if (builder.isPrivate == null) {
      throw new IllegalArgumentException("가계부 공개 여부는 필수 입니다.");
    }
    this.id = builder.getId();
    this.name = builder.getName();
    this.aliasName = builder.getAliasName();
    this.isPrivate = builder.getIsPrivate();
    this.createdAt = builder.getCreatedAt();
    this.updatedAt = builder.getUpdatedAt();
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
    return toBuilder().name(name).updatedAt(LocalDateTime.now()).build();
  }

  public AccountBookEntity updateAliasName(String aliasName) {
    return toBuilder().aliasName(aliasName).updatedAt(LocalDateTime.now()).build();
  }

  public AccountBookEntity updateIsPrivate(Boolean isPrivate) {
    return toBuilder().isPrivate(isPrivate).updatedAt(LocalDateTime.now()).build();
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
