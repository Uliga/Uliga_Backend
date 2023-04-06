package com.uliga.uliga_backend.domain.Category.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookData;
import com.uliga.uliga_backend.domain.Budget.model.Budget;
import com.uliga.uliga_backend.domain.Income.model.Income;
import com.uliga.uliga_backend.domain.Record.model.Record;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Budget> budgets = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AccountBookData> records = new ArrayList<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AccountBookData> incomes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

    @Builder
    public Category(Long id, String name, AccountBook accountBook) {
        this.id = id;
        this.name = name;
        this.accountBook = accountBook;
    }

    public void setName(String name) {
        this.name = name;
    }
}
