package com.uliga.uliga_backend.domain.Schedule.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AddScheduleResult;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectMapper mapper;

    @Transactional
    public AddScheduleResult addSchedule(Member member, AccountBook accountBook, List<CreateScheduleRequest> scheduleRequests) {
        List<ScheduleInfoQ> result = new ArrayList<>();
        for (CreateScheduleRequest scheduleRequest : scheduleRequests) {
            Schedule schedule = Schedule.builder()
                    .accountBook(accountBook)
                    .creator(member)
                    .value(scheduleRequest.getValue())
                    .isIncome(scheduleRequest.getIsIncome())
                    .notificationDate(scheduleRequest.getNotificationDate())
                    .name(scheduleRequest.getName())
                    .build();
            scheduleRepository.save(schedule);
            result.add(schedule.toInfoQ());
        }

        return AddScheduleResult.builder()
                .result(result).build();
    }

    @Transactional
    public UpdateScheduleRequest updateSchedule(Map<String, Object> updates) {
        UpdateScheduleRequest scheduleRequest = mapper.convertValue(updates, UpdateScheduleRequest.class);
        Schedule schedule = scheduleRepository.findById(scheduleRequest.getId()).orElseThrow(NotFoundByIdException::new);
        if (scheduleRequest.getIsIncome() != null) {
            schedule.updateIsIncome(scheduleRequest.getIsIncome());
        }
        if (scheduleRequest.getValue() != null) {
            schedule.updateValue(scheduleRequest.getValue());
        }
        if (scheduleRequest.getNotificationDate() != null) {
            schedule.updateDate(scheduleRequest.getNotificationDate());
        }
        if (scheduleRequest.getName() != null) {
            schedule.updateName(scheduleRequest.getName());
        }
        return scheduleRequest;
    }
}
