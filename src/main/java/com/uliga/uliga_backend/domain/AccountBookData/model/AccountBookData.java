package com.uliga.uliga_backend.domain.AccountBookData.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class AccountBookData extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "accountBookData_id")
    private Long id;

    private Long value;

    private String payment;

    private String account;

    private String memo;
    @Embedded
    private Date date;
    @Enumerated(EnumType.STRING)
    private AccountBookDataType type;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;
    public void setValue(Long value) {
        this.value = value;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setType(AccountBookDataType type) {
        this.type = type;
    }
}

