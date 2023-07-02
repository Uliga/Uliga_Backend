package com.uliga.uliga_backend.domain.Income.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.AccountBookData.dto.AccountBookDataDTO.CreateItemResult;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookData;
import com.uliga.uliga_backend.domain.AccountBookData.model.AccountBookDataType;
import com.uliga.uliga_backend.domain.Category.model.Category;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "income", catalog = "uliga_db")
public class Income extends AccountBookData {

    @Builder
    public Income(Long id, Long value, String payment, String account, String memo, Date date, Member creator, AccountBook accountBook, Category category) {
        super(id, value, payment, account, memo, date, AccountBookDataType.INCOME, creator, category, accountBook);
    }

    public String updateCategory(Category category) {
        super.setCategory(category);
        return category.getName();
    }

    public IncomeInfoQ toInfoQ() {
        return IncomeInfoQ.builder()
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

    public void updateValue(Long value) {
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
        super.setType(AccountBookDataType.RECORD);
    }
}

