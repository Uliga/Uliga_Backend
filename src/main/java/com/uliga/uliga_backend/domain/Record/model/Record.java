package com.uliga.uliga_backend.domain.Record.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookData;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
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
public class Record extends AccountBookData {
//    @Id
//    @GeneratedValue
//    @Column(name = "record_id")
//    private Long id;
//
//    private String memo;
//    private Long spend;
//
//    private String payment;
//
//    private String account;
//
//
//    @Embedded
//    private Date date;
//
//    private AccountBookDataType type = AccountBookDataType.RECORD;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member creator;
//
//    @ManyToOne
//    @JoinColumn(name = "accountBook_id")
//    private AccountBook accountBook;
//
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RecordComment> recordComments = new ArrayList<>();
    @Builder
    public Record(Long id, String memo, Long spend, String payment, String account, Date date, Member creator, AccountBook accountBook, Category category) {
        super(id, spend, payment, account, memo, date, AccountBookDataType.RECORD, creator, category, accountBook);
//        this.creator = creator;
//        this.accountBook = accountBook;
//        this.category = category;
    }

    public String updateCategory(Category category) {
        super.setCategory(category);
        return category.getName();
    }

    public RecordInfoQ toInfoQ() {
        return RecordInfoQ.builder()
                .id(super.getId())
                .account(super.getAccount())
                .value(super.getValue())
                .category(super.getCategory().getName())
                .payment(super.getPayment())
                .memo(super.getMemo())
                .year(super.getDate().getYear())
                .month(super.getDate().getMonth())
                .day(super.getDate().getDay())
                .creator(super.getCreator().getNickName())
                .build();
    }

    public void updateAccount(String account) {
        super.setAccount(account);
    }

    public void updateSpend(Long value) {
        super.setValue(value);
    }

    public void updatePayment(String payment) {
        super.setPayment(payment);
    }


    public void updateMemo(String memo) {
        super.setMemo(memo);
    }

    public void updateDate(String date) {
        String[] split = date.split("-");
        super.setDate(Date.builder()
                .year(Long.parseLong(split[0]))
                .month(Long.parseLong(split[1]))
                .day(Long.parseLong(split[2])).build());
    }


}
