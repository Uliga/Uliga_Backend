package com.uliga.uliga_backend.domain.Schedule.dao;

import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
