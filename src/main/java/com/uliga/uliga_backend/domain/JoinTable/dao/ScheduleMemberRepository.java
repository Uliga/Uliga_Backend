package com.uliga.uliga_backend.domain.JoinTable.dao;

import com.uliga.uliga_backend.domain.JoinTable.model.ScheduleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long>, JpaSpecificationExecutor<ScheduleMember> {
    List<ScheduleMember> findByScheduleId(Long scheduleId);

}
