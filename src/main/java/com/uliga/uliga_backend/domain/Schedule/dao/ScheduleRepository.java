package com.uliga.uliga_backend.domain.Schedule.dao;

import com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleAnalyzeQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id, " +
            "s.isIncome, "+
            "s.name, "+
            "s.notificationDate, " +
            "sm.value," +
            "m.id, " +
            "m.userName," +
            "s.accountBook.name) " +
            "FROM Schedule s JOIN ScheduleMember sm ON s.id = sm.schedule.id JOIN Member m on sm.member.id = m.id WHERE m.id = :id ORDER BY 31/(s.notificationDate - :curDate + 1) DESC ")
    List<ScheduleInfoQ> findByMemberId(@Param("id") Long id, @Param("curDate") int curDate);

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
            "s.accountBook.name) FROM Schedule s WHERE s.accountBook.id=:id ORDER BY 31/(s.notificationDate - :curDate + 1) DESC ")
    List<ScheduleInfoQ> findScheduleInfoByAccountBookId(@Param("id") Long id, @Param("curDate") int curDate);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ(" +
            "sm.member.id," +
            "sm.member.userName, " +
            "sm.value) FROM ScheduleMember sm WHERE sm.schedule.id = :id")
    List<ScheduleMemberInfoQ> findScheduleMemberInfoById(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(sm.value)) " +
            "FROM Schedule s " +
            "JOIN ScheduleMember sm ON sm.schedule.id = s.id " +
            "WHERE s.accountBook.id=:accountBookId " +
            "AND sm.member.id=:memberId " +
            "AND sm.value > 0")
    MonthlySumQ getMonthlyScheduleValue(@Param("accountBookId") Long accountBookId, @Param("memberId") Long memberId);

    void deleteById(Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleAnalyzeQ(s.name, s.notificationDate, sm.value) FROM Schedule s JOIN ScheduleMember sm ON s.id = sm.schedule.id WHERE s.accountBook.id=:accountBookId AND sm.member.id = :memberId AND sm.value > 0")
    List<ScheduleAnalyzeQ> findScheduleAnalyzeByAccountBookId(@Param("accountBookId") Long accountBookId, @Param("memberId") Long memberId);
}
