package com.uliga.uliga_backend.domain.Budget.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Budget.dto.NativeQ.BudgetInfoQ;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Budget extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "budget_id")
    private Long id;
    private Long value;
    private Long year;
    private Long month;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
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
