package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.tables.interfaces.IExpenseCategory;

import lombok.Getter;

@Getter
public class ExpenseCategoryEntity implements IExpenseCategory {
  private final Long id;
  private final String name;
  private final Long accountBookId;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  public ExpenseCategoryEntity(IExpenseCategory value) {
    this.id = value.getId();
    this.name = value.getName();
    this.accountBookId = value.getAccountBookId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .accountBookId(accountBookId)
        .name(name)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public ExpenseCategoryEntity updateName(String name) {
    return toBuilder().name(name).build();
  }

  @Getter
  public static class Builder implements IExpenseCategory {
    private Long id;
    private String name;
    private Long accountBookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder accountBookId(Long accountBookId) {
      this.accountBookId = accountBookId;
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

    public ExpenseCategoryEntity build() {
      return new ExpenseCategoryEntity(this);
    }
  }
}
