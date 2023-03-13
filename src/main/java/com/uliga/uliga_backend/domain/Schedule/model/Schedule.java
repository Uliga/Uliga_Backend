package com.uliga.uliga_backend.domain.Schedule.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.Member.model.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "schedule_id")
    private Long id;

    private Long notificationDate;
    private Long dueDate;

    private Long value;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

}
