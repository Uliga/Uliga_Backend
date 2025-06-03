package com.uliga.uliga_backend.domain.account_book.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.uliga.uliga_backend.domain.account_book.dto.AccountBookDTO.SimpleAccountBookInfo;
import com.uliga.uliga_backend.domain.account_book_data.model.AccountBookData;
import com.uliga.uliga_backend.domain.budget.model.Budget;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.domain.common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.join_table.model.AccountBookMember;
import com.uliga.uliga_backend.domain.schedule.model.Schedule;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountBook extends BaseTimeEntity {
  @Id
  private Long id;

  private Boolean isPrivate;

  private String name;

  private String relationShip;

  @Builder
  public AccountBook(Boolean isPrivate, String name, String relationShip) {

    this.isPrivate = isPrivate;
    this.name = name;
    this.relationShip = relationShip;
  }

  private List<Budget> budgets = new ArrayList<>();

  private List<AccountBookData> incomes = new ArrayList<>();

  private List<AccountBookMember> members = new ArrayList<>();

  private List<Schedule> schedules = new ArrayList<>();

  private List<AccountBookData> records = new ArrayList<>();

  private List<Category> categories = new ArrayList<>();

  public SimpleAccountBookInfo toInfoDto() {
    return SimpleAccountBookInfo.builder()
        .id(id)
        .isPrivate(isPrivate)
        .name(name)
        .relationShip(relationShip)
        .build();
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setRelationShip(String relationShip) {
    this.relationShip = relationShip;
  }
}
