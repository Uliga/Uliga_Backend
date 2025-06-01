package com.uliga.uliga_backend.domain.account_book_data.model;

import com.uliga.uliga_backend.domain.account_book.model.AccountBook;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.domain.common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.common.Date;
import com.uliga.uliga_backend.domain.member.model.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "account_book_data", catalog = "uliga_db")
public class AccountBookData extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_book_data_id")
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
    @JoinColumn(name = "account_book_id")
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
