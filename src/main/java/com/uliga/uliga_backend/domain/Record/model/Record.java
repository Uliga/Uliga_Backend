package com.uliga.uliga_backend.domain.Record.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBook.model.PaymentType;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.RecordComment.model.RecordComment;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Record extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "record_id")
    private Long id;

    private String memo;
    private Long spend;

    private PaymentType payment;

    private String account;


    @Embedded
    private Date date;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RecordComment> recordComments = new ArrayList<>();
    @Builder
    public Record(Long id, String memo, Long spend, PaymentType payment, String account, Date date, Member creator, AccountBook accountBook, Category category) {
        this.id = id;
        this.memo = memo;
        this.spend = spend;
        this.payment = payment;
        this.account = account;
        this.date = date;
        this.creator = creator;
        this.accountBook = accountBook;
        this.category = category;
    }
}
