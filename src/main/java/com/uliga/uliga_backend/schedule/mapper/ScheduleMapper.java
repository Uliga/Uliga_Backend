package com.uliga.uliga_backend.schedule.mapper;

import java.util.HashMap;
import java.util.List;

import com.uliga.uliga_backend.schedule.dto.NativeQ.ScheduleMonthSum;

public interface ScheduleMapper {
  List<ScheduleMonthSum> getScheduleMonthSum(HashMap<String, Object> map);
}
