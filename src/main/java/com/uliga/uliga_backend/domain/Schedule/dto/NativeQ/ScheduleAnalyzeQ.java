package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;


import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleAnalyzeQ {
    private String name;
    private Long day;
    private Long value;

    public ScheduleAnalyzeQ(String name, Long day, Long value) {
        this.name = name;
        this.day = day;
        this.value = value;
    }
}
