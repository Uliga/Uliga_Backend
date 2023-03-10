package com.uliga.uliga_backend.domain.Record.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.RecordComment.model.RecordComment;
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

    private String payment;

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
    public Record(Long id, String memo, Long spend, String payment, String account, Date date, Member creator, AccountBook accountBook, Category category) {
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

    public String updateCategory(Category category) {
        this.category = category;
        return category.getName();
    }

    public RecordInfoQ toInfoQ() {
        return RecordInfoQ.builder()
                .id(id)
                .account(account)
                .value(spend)
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

    public void updateSpend(Long spend) {
        this.spend = spend;
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
