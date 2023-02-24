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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "year", column = @Column(name = "due_year")),
            @AttributeOverride(name = "month", column = @Column(name = "due_month")),
            @AttributeOverride(name="day", column = @Column(name = "due_day"))
    })
    private Date dueDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "year", column = @Column(name = "notification_year")),
            @AttributeOverride(name = "month", column = @Column(name = "notification_month")),
            @AttributeOverride(name="day", column = @Column(name = "notification_day"))
    })
    private Date notificationDate;

    private Long value;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "accountBook_id")
    private AccountBook accountBook;

}
