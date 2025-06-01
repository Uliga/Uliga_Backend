package com.uliga.uliga_backend.domain.budget.model;

import com.uliga.uliga_backend.domain.account_book.model.AccountBook;
import com.uliga.uliga_backend.domain.budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.domain.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "budget", catalog = "uliga_db")
public class Budget extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long id;
    private Long value;
    private Long year;
    private Long month;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "account_book_id")
    private AccountBook accountBook;

    @Builder
    public Budget(Long value, Long year, Long month, Category category, AccountBook accountBook) {
        this.value = value;
        this.year = year;
        this.month = month;
        this.category = category;
        this.accountBook = accountBook;
    }

    public BudgetInfoQ toInfoQ() {
        return BudgetInfoQ.builder()
                .id(id)
                .value(value)
                .month(month)
                .year(year).build();
    }

    public void updateValue(Long value) {
        this.value = value;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}
