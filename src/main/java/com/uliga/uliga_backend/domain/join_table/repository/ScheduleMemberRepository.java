package com.uliga.uliga_backend.domain.join_table.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uliga.uliga_backend.domain.join_table.model.ScheduleMember;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findByScheduleId(Long scheduleId);

    void deleteAllByMemberId(Long memberId);

}
