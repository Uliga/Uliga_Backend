package com.uliga.uliga_backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uliga.uliga_backend.jooq.enums.Frequency;
import com.uliga.uliga_backend.jooq.tables.interfaces.IFixedExpense;

import lombok.Getter;

@Getter
public class FixedExpenseEntity implements IFixedExpense {

  private final Long id;
  private final String name;
  private final Long value;
  private final Long userId;
  private final Long accountBookId;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private final LocalDate startDate;
  private final Frequency frequency;

  public FixedExpenseEntity(IFixedExpense value) {
    this.id = value.getId();
    this.name = value.getName();
    this.value = value.getValue();
    this.userId = value.getUserId();
    this.accountBookId = value.getAccountBookId();
    this.createdAt = value.getCreatedAt();
    this.updatedAt = value.getUpdatedAt();
    this.startDate = value.getStartDate();
    this.frequency = value.getFrequency();
  }

  public static Builder builder() {
    return new Builder();
  }

  private Builder toBuilder() {
    return new Builder()
        .id(id)
        .name(name)
        .value(value)
        .userId(userId)
        .accountBookId(accountBookId)
        .createdAt(createdAt)
        .updatedAt(updatedAt)
        .startDate(startDate)
        .frequency(frequency);
  }

  public FixedExpenseEntity updateName(String name) {
    return toBuilder().name(name).build();
  }

  public FixedExpenseEntity updateValue(Long value) {
    return toBuilder().value(value).build();
  }

  public FixedExpenseEntity updateStartDate(LocalDate startDate) {
    return toBuilder().startDate(startDate).build();
  }

  public FixedExpenseEntity updateFrequency(Frequency frequency) {
    return toBuilder().frequency(frequency).build();
  }

  @Getter
  public static class Builder implements IFixedExpense {
    private Long id;
    private String name;
    private Long value;
    private Long userId;
    private Long accountBookId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate startDate;
    private Frequency frequency;

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

    public Builder value(Long value) {
      this.value = value;
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

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Builder startDate(LocalDate startDate) {
      this.startDate = startDate;
      return this;
    }

    public Builder frequency(Frequency frequency) {
      this.frequency = frequency;
      return this;
    }

    public FixedExpenseEntity build() {
      return new FixedExpenseEntity(this);
    }
  }
}
