package com.uliga.uliga_backend.domain.record.model;

import java.util.ArrayList;
import java.util.List;

import com.uliga.uliga_backend.domain.account_book.model.AccountBook;
import com.uliga.uliga_backend.domain.account_book_data.dto.AccountBookDataDTO.CreateItemResult;
import com.uliga.uliga_backend.domain.account_book_data.model.AccountBookData;
import com.uliga.uliga_backend.domain.account_book_data.model.AccountBookDataType;
import com.uliga.uliga_backend.domain.category.model.Category;
import com.uliga.uliga_backend.domain.common.Date;
import com.uliga.uliga_backend.domain.member.model.Member;
import com.uliga.uliga_backend.domain.record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.record_comment.model.RecordComment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "record", catalog = "uliga_db")
public class Record extends AccountBookData {

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RecordComment> recordComments = new ArrayList<>();

    @Builder
    public Record(Long id, String memo, Long spend, String payment, String account, Date date, Member creator,
            AccountBook accountBook, Category category) {
        super(id, spend, payment, account, memo, date, AccountBookDataType.RECORD, creator, category, accountBook);
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

    public CreateItemResult toCreateItemResult() {
        return CreateItemResult.builder()
                .id(super.getId())
                .account(super.getAccount())
                .value(super.getValue())
                .category(super.getCategory().getName())
                .payment(super.getPayment())
                .memo(super.getMemo())
                .year(super.getDate().getYear())
                .month(super.getDate().getMonth())
                .day(super.getDate().getDay())
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

    public void updateType() {
        super.setType(AccountBookDataType.INCOME);
    }
}
