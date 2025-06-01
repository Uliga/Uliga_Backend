package com.uliga.uliga_backend.domain.account_book.model;

import java.util.ArrayList;
import java.util.List;

import com.uliga.uliga_backend.domain.account_book.dto.AccountBookDTO.SimpleAccountBookInfo;
import com.uliga.uliga_backend.domain.account_book_data.model.AccountBookData;
import com.uliga.uliga_backend.domain.budget.model.Budget;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.domain.common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.join_table.model.AccountBookMember;
import com.uliga.uliga_backend.domain.schedule.model.Schedule;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "account_book", catalog = "uliga_db")
public class AccountBook extends BaseTimeEntity {
  @Id
  @Column(name = "account_book_id")
  private Long id;

  @Column(name = "is_private")
  private Boolean isPrivate;

  private String name;
  @Column(name = "relation_ship")
  private String relationShip;

  @Builder
  public AccountBook(Boolean isPrivate, String name, String relationShip) {

    this.isPrivate = isPrivate;
    this.name = name;
    this.relationShip = relationShip;
  }

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Budget> budgets = new ArrayList<>();

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<AccountBookData> incomes = new ArrayList<>();

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<AccountBookMember> members = new ArrayList<>();

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<Schedule> schedules = new ArrayList<>();

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  private List<AccountBookData> records = new ArrayList<>();

  @OneToMany(mappedBy = "accountBook", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
