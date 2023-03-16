package com.uliga.uliga_backend.domain.Schedule.dto.NativeQ;

import com.uliga.uliga_backend.domain.Common.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ScheduleInfoQ {
    @Schema(description = "스케줄 아이디")
    private Long id;
    @Schema(description = "알림 받을 날짜", defaultValue = "3")
    private Long notificationDay;
    @Schema(description = "금융 일정 이름", defaultValue = "금융 일정 이름")
    private String name;
    @Schema(description = "수입/지출 여부")
    private Boolean isIncome;
    @Schema(description = "나가거나/들어오는 값(null이면 변동이라는 뜻)")
    private Long value;
    @Schema(description = "작성자", defaultValue = "creatorNickname")
    private String creator;
    @Schema(description = "가계부 이름",defaultValue = "accountBookName")
    private String accountBookName;
    @Builder
    public ScheduleInfoQ(Long id, Boolean isIncome, String name, Long nDay, Long value, String creator, String accountBookName) {
        this.id = id;
        this.isIncome = isIncome;
        this.name = name;
        this.notificationDay = nDay;
        this.value = value;
        this.creator = creator;
        this.accountBookName = accountBookName;
    }
}
