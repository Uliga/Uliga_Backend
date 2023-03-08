package com.uliga.uliga_backend.domain.Income.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Income {
    @Id
    @GeneratedValue
    @Column(name = "income_id")
    private Long id;

    private Long value;

    private String payment;

    private String account;

    private String memo;
    @Embedded
    private Date date;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

    @Builder
    public Income(Long id, Long value, String payment, String account, String memo, Date date, Member creator, AccountBook accountBook, Category category) {

        this.id = id;
        this.value = value;
        this.payment = payment;
        this.account = account;
        this.memo = memo;
        this.date = date;
        this.creator = creator;
        this.accountBook = accountBook;
        this.category = category;
    }
}

