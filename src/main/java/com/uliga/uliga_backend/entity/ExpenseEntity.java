package com.uliga.uliga_backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.tables.interfaces.IExpense;
import com.uliga.uliga_backend.util.KstDateUtils;

import lombok.Getter;

@Getter
public class ExpenseEntity implements IExpense {
  private final Long id;
  private final Long value;
  private final String expensePayee;
  private final String memo;
  private final LocalDate date;
  private final Long userId;
  private final Long expenseCategoryId;
  private final Long accountBookId;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final String year;
  private final String month;
  private final String week;

  public ExpenseEntity(IExpense value) {
    this.id = value.getId();
    this.value = value.getValue();
    this.expensePayee = value.getExpensePayee();
    this.memo = value.getMemo();
    this.date = value.getDate();
    this.userId = value.getUserId();
    this.expenseCategoryId = value.getExpenseCategoryId();
    this.accountBookId = value.getAccountBookId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.year = value.getYear();
    this.month = value.getMonth();
    this.week = value.getWeek();
  }

  private ExpenseEntity(Builder value) {
    this.id = value.getId();
    this.value = value.getValue();
    this.expensePayee = value.getExpensePayee();
    this.memo = value.getMemo();
    this.date = value.getDate();
    this.userId = value.getUserId();
    this.expenseCategoryId = value.getExpenseCategoryId();
    this.accountBookId = value.getAccountBookId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.year = value.getYear();
    this.month = value.getMonth();
    this.week = value.getWeek();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .value(value)
        .expensePayee(expensePayee)
        .memo(memo)
        .date(date)
        .userId(userId)
        .expenseCategoryId(expenseCategoryId)
        .accountBookId(accountBookId)
        .createdAt(createdAt)
        .updatedAt(updatedAt);
  }

  public ExpenseEntity updateValue(Long value) {
    return toBuilder().value(value).build();
  }

  public ExpenseEntity updateMemo(String memo) {
    return toBuilder().memo(memo).build();
  }

  public ExpenseEntity updateExpenseCategoryId(Long expenseCategoryId) {
    return toBuilder().expenseCategoryId(expenseCategoryId).build();
  }

  public ExpenseEntity updateDate(LocalDate date) {
    return toBuilder().date(date).build();
  }

  @Getter
  public static class Builder implements IExpense {
    private Long id;
    private Long value;
    private String expensePayee;
    private String memo;
    private LocalDate date;
    private Long userId;
    private Long expenseCategoryId;
    private Long accountBookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String year;
    private String month;
    private String week;

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

    public Builder expensePayee(String expensePayee) {
      this.expensePayee = expensePayee;
      return this;
    }

    public Builder memo(String memo) {
      this.memo = memo;
      return this;
    }

    public Builder date(LocalDate date) {
      this.date = date;
      this.year = KstDateUtils.getYearString(date);
      this.month = KstDateUtils.getMonthString(date);
      this.week = KstDateUtils.getWeekString(date);
      return this;
    }

    public Builder userId(Long userId) {
      this.userId = userId;
      return this;
    }

    public Builder expenseCategoryId(Long expenseCategoryId) {
      this.expenseCategoryId = expenseCategoryId;
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

    public ExpenseEntity build() {
      return new ExpenseEntity(this);
    }
  }
}
