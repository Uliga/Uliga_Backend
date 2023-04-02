package com.uliga.uliga_backend.domain.Schedule.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.AddScheduleResult;
import com.uliga.uliga_backend.domain.AccountBook.dto.AccountBookDTO.GetAccountBookSchedules;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.JoinTable.dao.ScheduleMemberRepository;
import com.uliga.uliga_backend.domain.JoinTable.model.ScheduleMember;
import com.uliga.uliga_backend.domain.Member.dao.MemberRepository;
import com.uliga.uliga_backend.domain.Member.dto.MemberDTO.NotificationInfo;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleMapper;
import com.uliga.uliga_backend.domain.Schedule.dao.ScheduleRepository;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMonthSum;
import com.uliga.uliga_backend.domain.Schedule.exception.InvalidScheduleDelete;
import com.uliga.uliga_backend.domain.Schedule.model.Schedule;
import com.uliga.uliga_backend.global.error.exception.NotFoundByIdException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.*;
import static com.uliga.uliga_backend.global.common.constants.UserConstants.NOTIFICATION_EXPIRE_TIME;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    @Transactional
    public GetMemberSchedules getMemberSchedule(Long id) {
        return GetMemberSchedules.builder()
                .schedules(scheduleRepository.findByMemberId(id)).build();
    }

    @Transactional
    public AddScheduleResult addSchedule(Member member, AccountBook accountBook, List<CreateScheduleRequest> scheduleRequests) throws JsonProcessingException {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        List<CreateScheduleRequest> result = new ArrayList<>();
        List<Member> memberByAccountBookId = memberRepository.findMemberByAccountBookId(accountBook.getId());
        Map<Long, Member> memberMap = new HashMap<>();
        List<ScheduleMember> toSave = new ArrayList<>();
        for (Member m : memberByAccountBookId) {
            memberMap.put(m.getId(), m);
        }
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
            for (Assignment assignment : scheduleRequest.getAssignments()) {
                ScheduleMember build = ScheduleMember.builder()
                        .member(memberMap.get(assignment.getId()))
                        .schedule(schedule)
                        .value(assignment.getValue()).build();
                toSave.add(build);
                NotificationInfo notificationInfo = NotificationInfo.builder()
                        .scheduleName(scheduleRequest.getName())
                        .day(scheduleRequest.getNotificationDate())
                        .creatorName(member.getUserName())
                        .value(assignment.getValue()).build();
                setOperations.add(memberMap.get(assignment.getId()).getNickName(), mapper.writeValueAsString(notificationInfo));
                redisTemplate.expire(memberMap.get(assignment.getId()).getNickName(), NOTIFICATION_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            }
            result.add(scheduleRequest);
        }
        // 알림 부분 추가해야함
        scheduleMemberRepository.saveAll(toSave);
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
        if (scheduleRequest.getAssignments() != null) {
            List<ScheduleMember> byScheduleId = scheduleMemberRepository.findByScheduleId(schedule.getId());
            for (ScheduleMember sm : byScheduleId) {
                sm.updateValue(scheduleRequest.getAssignments().get(Long.toString(sm.getMember().getId())));
            }

        }
        return scheduleRequest;
    }

    @Transactional
    public ScheduleDetail getScheduleDetails(Long id) {

        return ScheduleDetail.builder()
                .info(scheduleRepository.findScheduleInfoById(id))
                .assignments(scheduleRepository.findScheduleMemberInfoById(id)).build();
    }

    @Transactional
    public GetAccountBookSchedules getAccountBookSchedules(Long accountBookId) {
        List<ScheduleDetail> result = new ArrayList<>();
        List<ScheduleInfoQ> byAccountBookId = scheduleRepository.findScheduleInfoByAccountBookId(accountBookId);
        for (ScheduleInfoQ s : byAccountBookId) {
            ScheduleDetail scheduleDetail = ScheduleDetail.builder().info(s).assignments(scheduleRepository.findScheduleMemberInfoById(s.getId())).build();
            result.add(scheduleDetail);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", accountBookId);
        GetAccountBookSchedules accountBookSchedules = GetAccountBookSchedules.builder().build();
        accountBookSchedules.setSchedules(result);
        List<ScheduleMonthSum> scheduleMonthSum = scheduleMapper.getScheduleMonthSum(map);
        for (ScheduleMonthSum sms : scheduleMonthSum) {
            if (sms.getIsIncome()) {
                accountBookSchedules.setIncomeSum(sms.getValue());
            } else {
                accountBookSchedules.setRecordSum(sms.getValue());
            }
        }
        return accountBookSchedules;
    }

    @Transactional
    public void deleteSchedule(Long id, Long currentMemberId) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (schedule.getCreator().getId().equals(currentMemberId)) {

            scheduleRepository.deleteById(id);
        } else {
            throw new InvalidScheduleDelete();
        }

    }
}
