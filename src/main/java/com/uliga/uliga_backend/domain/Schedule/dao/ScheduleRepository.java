package com.uliga.uliga_backend.domain.Schedule.dao;

import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id, " +
            "s.isIncome, "+
            "s.name, "+
            "s.notificationDate, " +
            "sm.value," +
            "m.id, " +
            "m.userName," +
            "s.accountBook.name) " +
            "FROM Schedule s JOIN ScheduleMember sm ON s.id = sm.schedule.id JOIN Member m on sm.member.id = m.id WHERE m.id = :id ORDER BY s.notificationDate ASC ")
    List<ScheduleInfoQ> findByMemberId(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id," +
            "s.isIncome," +
            "s.name," +
            "s.notificationDate," +
            "s.value," +
            "s.creator.id," +
            "s.creator.userName," +
            "s.accountBook.name) FROM Schedule s WHERE s.id = :id")
    ScheduleInfoQ findScheduleInfoById(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id," +
            "s.isIncome," +
            "s.name," +
            "s.notificationDate," +
            "s.value," +
            "s.creator.id," +
            "s.creator.userName," +
            "s.accountBook.name) FROM Schedule s WHERE s.accountBook.id=:id ORDER BY s.notificationDate ASC ")
    List<ScheduleInfoQ> findScheduleInfoByAccountBookId(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ(" +
            "sm.member.id," +
            "sm.member.userName, " +
            "sm.value) FROM ScheduleMember sm WHERE sm.schedule.id = :id")
    List<ScheduleMemberInfoQ> findScheduleMemberInfoById(@Param("id") Long id);

    void deleteById(Long id);
}
