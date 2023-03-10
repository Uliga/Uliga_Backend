package com.uliga.uliga_backend.domain.Budget.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import jakarta.persistence.*;
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

    private Long month;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

}
