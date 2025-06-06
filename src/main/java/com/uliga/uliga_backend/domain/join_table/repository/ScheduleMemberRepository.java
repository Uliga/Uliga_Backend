package com.uliga.uliga_backend.domain.join_table.repository;

import java.util.List;

import com.uliga.uliga_backend.domain.join_table.model.ScheduleMember;

public interface ScheduleMemberRepository {
  List<ScheduleMember> findByScheduleId(Long scheduleId);

  void deleteAllByMemberId(Long memberId);

}
