package com.uliga.uliga_backend.domain.Schedule.dao;

import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ(" +
            "s.id, " +
            "s.dueDate.year," +
            "s.dueDate.month," +
            "s.dueDate.day, " +
            "s.notificationDate.year," +
            "s.notificationDate.month," +
            "s.notificationDate.day, " +
            "s.value, " +
            "m.nickName) " +
            "FROM Schedule s " +
            "JOIN Member m ON m.id = s.creator.id " +
            "WHERE s.accountBook.id=:id")
    List<ScheduleInfoQ> findByAccountBookId(@Param("id") Long id);
}
