package com.uliga.uliga_backend.entity;

import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.tables.interfaces.IBudget;

import lombok.Getter;

@Getter
public class BudgetEntity implements IBudget {
  private final Long id;
  private final Long value;
  private final Long year;
  private final Long month;
  private final Long accountBookId;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final Long expenseCategoryId;

  public BudgetEntity(IBudget value) {
    this.id = value.getId();
    this.value = value.getValue();
    this.year = value.getYear();
    this.month = value.getMonth();
    this.accountBookId = value.getAccountBookId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.expenseCategoryId = value.getExpenseCategoryId();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .value(value)
        .year(year)
        .month(month)
        .accountBookId(accountBookId)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .expenseCategoryId(expenseCategoryId);
  }

  public BudgetEntity updateValue(Long value) {
    return toBuilder().value(value).build();
  }

  public BudgetEntity updateExpenseCategoryId(Long expenseCategoryId) {
    return toBuilder().expenseCategoryId(expenseCategoryId).build();
  }

  @Getter
  public static class Builder implements IBudget {
    private Long id;
    private Long value;
    private Long year;
    private Long month;
    private Long accountBookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long expenseCategoryId;

    private Builder() {
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder value(Long value) {
      this.value = value;
      return this;
    }

    public Builder year(Long year) {
      this.year = year;
      return this;
    }

    public Builder month(Long month) {
      this.month = month;
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

    public Builder expenseCategoryId(Long expenseCategoryId) {
      this.expenseCategoryId = expenseCategoryId;
      return this;
    }

    public BudgetEntity build() {
      return new BudgetEntity(this);
    }
  }
}
