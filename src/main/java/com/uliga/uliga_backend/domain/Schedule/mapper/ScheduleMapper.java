package com.uliga.uliga_backend.domain.schedule.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uliga.uliga_backend.domain.schedule.dto.NativeQ.ScheduleMonthSum;

@Mapper
public interface ScheduleMapper {
    List<ScheduleMonthSum> getScheduleMonthSum(HashMap<String, Object> map);
}
