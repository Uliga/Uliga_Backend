package com.uliga.uliga_backend.domain.JoinTable.model;

import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "schedule_member", catalog = "uliga_db")
public class ScheduleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private Long value;

    @Builder
    public ScheduleMember(Long id, Member member, Schedule schedule, Long value) {
        this.id = id;
        this.member = member;
        this.schedule = schedule;
        this.value = value;
    }

    public void updateValue(Long value) {
        this.value = value;
    }
}
