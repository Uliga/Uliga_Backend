package com.uliga.uliga_backend.domain.Schedule.application;

import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void addSchedule() {

    }
}
