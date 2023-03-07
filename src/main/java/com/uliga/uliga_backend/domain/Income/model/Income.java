package com.uliga.uliga_backend.domain.Income.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
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
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;
}

