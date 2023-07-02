package com.uliga.uliga_backend.domain.Schedule.mapper;

import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMonthSum;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<ScheduleMonthSum> getScheduleMonthSum(HashMap<String, Object> map);
}
