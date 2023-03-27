package com.uliga.uliga_backend.domain.Schedule.exception;

public class InvalidScheduleDelete extends IllegalArgumentException {
    public InvalidScheduleDelete() {
        super("금융 일정 생성자가 아니라서 해당 일정을 삭제할 수 없습니다");
    }

    public InvalidScheduleDelete(String s) {
        super(s);
    }
}
