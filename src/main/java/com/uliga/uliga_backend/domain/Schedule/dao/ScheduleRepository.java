package com.uliga.uliga_backend.domain.Schedule.dao;

import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id, " +
            "s.isIncome, "+
            "s.name, "+
            "s.notificationDate, " +
            "sm.value, " +
            "m.nickName," +
            "s.accountBook.name) " +
            "FROM Schedule s JOIN ScheduleMember sm ON s.id = sm.schedule.id JOIN Member m on sm.member.id = m.id WHERE m.id = :id")
    List<ScheduleInfoQ> findByMemberId(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id," +
            "s.isIncome," +
            "s.name," +
            "s.notificationDate," +
            "s.value," +
            "s.creator.nickName," +
            "s.accountBook.name) FROM Schedule s WHERE s.id = :id")
    ScheduleInfoQ findScheduleInfoById(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ(" +
            "sm.member.nickName, " +
            "sm.value) FROM ScheduleMember sm WHERE sm.schedule.id = :id")
    List<ScheduleMemberInfoQ> findScheduleMemberInfoById(@Param("id") Long id);
}
