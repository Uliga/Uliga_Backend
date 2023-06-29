package com.uliga.uliga_backend.domain.Schedule.model;

import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Common.Date;
import com.uliga.uliga_backend.domain.JoinTable.model.ScheduleMember;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule", catalog = "uliga_db")
public class Schedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;
    private String name;
    @Column(name = "is_income")
    private Boolean isIncome;
    @Column(name = "notification_date")
    private Long notificationDate;

    private Long value;
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;

    @ManyToOne
    @JoinColumn(name = "account_book_id")
    private AccountBook accountBook;
    @Builder
    public Schedule(Long id, String name, Boolean isIncome, Long notificationDate, Long value, Member creator, AccountBook accountBook) {
        this.id = id;
        this.name = name;
        this.isIncome = isIncome;
        this.notificationDate = notificationDate;
        this.value = value;
        this.creator = creator;
        this.accountBook = accountBook;
    }

    public ScheduleInfoQ toInfoQ() {
        return ScheduleInfoQ.builder()
                .id(id)
                .nDay(notificationDate)
                .value(value)
                .name(name)
                .isIncome(isIncome)
                .creator(creator.getNickName()).build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateValue(Long value) {
        this.value = value;
    }

    public void updateDate(Long notificationDate) {
        this.notificationDate = notificationDate;
    }

    public void updateIsIncome(Boolean isIncome) {
        this.isIncome = isIncome;
    }
}
