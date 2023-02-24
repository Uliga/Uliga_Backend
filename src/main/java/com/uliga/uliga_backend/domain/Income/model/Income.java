package com.uliga.uliga_backend.domain.Income.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Common.Date;
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

    @Embedded
    private Date date;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;
}

