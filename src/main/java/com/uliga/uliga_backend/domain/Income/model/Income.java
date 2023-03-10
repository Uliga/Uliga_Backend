package com.uliga.uliga_backend.domain.Income.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
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

    public String updateCategory(Category category) {
        this.category = category;
        return category.getName();
    }

    public IncomeInfoQ toInfoQ() {
        return IncomeInfoQ.builder()
                .id(id)
                .account(account)
                .value(value)
                .category(category.getName())
                .payment(payment)
                .memo(memo)
                .year(date.getYear())
                .month(date.getMonth())
                .day(date.getDay())
                .creator(creator.getNickName())
                .build();
    }
    public void updateAccount(String account) {
        this.account = account;
    }

    public void updateValue(Long value) {
        this.value = value;
    }

    public void updatePayment(String payment) {
        this.payment = payment;
    }


    public void updateMemo(String memo) {
        this.memo = memo;
    }

    public void updateDate(String date) {
        String[] split = date.split("-");
        this.date = Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build();
    }
}

