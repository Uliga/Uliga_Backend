package com.uliga.uliga_backend.domain.JoinTable.repository;

import com.uliga.uliga_backend.domain.JoinTable.model.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findByScheduleId(Long scheduleId);

    void deleteAllByMemberId(Long memberId);

}
