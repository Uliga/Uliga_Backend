package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.tables.interfaces.IFixedExpenseUser;

import lombok.Getter;

@Getter
public class FixedExpenseUserEntity implements IFixedExpenseUser {
  private final Long id;
  private final Long userId;
  private final Long accountBookId;
  private final Long fixedExpenseId;
  private final Long value;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public FixedExpenseUserEntity(IFixedExpenseUser value) {
    this.id = value.getId();
    this.userId = value.getUserId();
    this.accountBookId = value.getAccountBookId();
    this.fixedExpenseId = value.getFixedExpenseId();
    this.value = value.getValue();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .userId(userId)
        .accountBookId(accountBookId)
        .fixedExpenseId(fixedExpenseId)
        .value(value)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public FixedExpenseUserEntity updateValue(Long value) {
    return toBuilder().value(value).build();
  }

  @Getter
  public static class Builder implements IFixedExpenseUser {
    private Long id;
    private Long userId;
    private Long accountBookId;
    private Long fixedExpenseId;
    private Long value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public Builder accountBookId(Long accountBookId) {
      this.accountBookId = accountBookId;
      return this;
    }

    public Builder fixedExpenseId(Long fixedExpenseId) {
      this.fixedExpenseId = fixedExpenseId;
      return this;
    }

    public Builder value(Long value) {
      this.value = value;
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

    public FixedExpenseUserEntity build() {
      return new FixedExpenseUserEntity(this);
    }

  }
}
