package com.uliga.uliga_backend.domain.Schedule.dto;

import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleAnalyzeQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleInfoQ;
import com.uliga.uliga_backend.domain.Schedule.dto.NativeQ.ScheduleMemberInfoQ;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Map;

public class ScheduleDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleInfo {
        private Long id;
        private Long notificationDate;
        private Long value;
        private String creator;

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "금융 일정 생성 요청")
    public static class CreateScheduleRequest {
        @Schema(description = "금융 일정 이름")
        @NotNull
        private String name;
        @Schema(description = "수입인지 지출인지")
        @NotNull
        private Boolean isIncome;
        @Schema(description = "일정 날짜")
        @NotNull
        private Long notificationDate;
        @Schema(description = "일정 값")
        private Long value;
        @Schema(description = "금액 할당에 대한 정보")
        private List<Assignment> assignments;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "멤버 금액 할당")
    public static class Assignment {
        @Schema(description = "멤버 아이디")
        private Long id;
        @Schema(description = "멤버 이름")
        private String username;
        @Schema(description = "할당량")
        private Long value;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateScheduleRequest {
        @Schema(description = "아이디")
        private Long id;
        @Schema(description = "금융 일정 이름")
        private String name;
        @Schema(description = "수입/지출 여부")
        private Boolean isIncome;
        @Schema(description = "알림받을 날짜")
        private Long notificationDate;
        @Schema(description = "금액")
        private Long value;
        @Schema(description = "멤버들 할당 금액")
        private Map<String, Long> assignments;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetMemberSchedules {
        @Schema(description = "금융 일정들")
        private List<ScheduleInfoQ> schedules;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDetail {
        @Schema(description = "금융 일정 정보")
        private ScheduleInfoQ info;

        @Schema(description = "멤버 할당 정보")
        private List<ScheduleMemberInfoQ> assignments;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "금융 일정 삭제 요청 데이터")
    public static class ScheduleDeleteRequest {
        @Schema(description = "삭제할 금융 일정 아이디")
        private Long id;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddSchedules {
        @NotNull
        private Long id;
        @NotNull
        private List<CreateScheduleRequest> schedules;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddScheduleResult {
        private List<CreateScheduleRequest> result;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAccountBookSchedules {
        private Long incomeSum;
        private Long recordSum;
        private List<ScheduleDetail> schedules;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountBookScheduleAnalyze {
        private List<ScheduleAnalyzeQ> schedules;
        private Long sum;
    }
}
