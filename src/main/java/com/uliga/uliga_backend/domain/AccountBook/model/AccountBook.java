package com.uliga.uliga_backend.domain.AccountBook.model;

import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.SimpleAccountBookInfo;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookData;
import com.uliga.uliga_backend.domain.Budget.model.Budget;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.JoinTable.model.AccountBookMember;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.model.Record;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class AccountBook extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "accountBook_id")
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
}

