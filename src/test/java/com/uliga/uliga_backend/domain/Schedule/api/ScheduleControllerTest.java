package com.uliga.uliga_backend.domain.Schedule.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Record.api.RecordController;
import com.uliga.uliga_backend.domain.Schedule.application.ScheduleService;
import com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static com.uliga.uliga_backend.domain.Schedule.dto.ScheduleDTO.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@WebMvcTest(ScheduleController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ScheduleControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    ScheduleService scheduleService;
    private final String BASE_URL = "/schedule";

//    @Test
//    @WithMockCustomUser
//    @DisplayName("멤버 금융 일정 조회 성공 테스트")
//     void getMemberSchedules() throws Exception{
//        // given
//        GetMemberSchedules result = GetMemberSchedules.builder().build();
//
//        // when
//        doReturn(result).when(scheduleService).getMemberSchedule(any());
//        // then
//        mvc.perform(get(BASE_URL)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("금융 일정 세부 조회 성공 테스트")
//     void getScheduleInfos() throws Exception{
//        // given
//        ScheduleDetail result = ScheduleDetail.builder().build();
//
//        // when
//        doReturn(result).when(scheduleService).getScheduleDetails(any());
//
//        // then
//        mvc.perform(get(BASE_URL + "/1")
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
//
//    @Test
//    @WithMockCustomUser
//    @DisplayName("금융 일정 업데이트 성공 테스트")
//     void updateSchedule() throws Exception{
//        // given
//        Map<String, Object> updates = new HashMap<>();
//        String value = mapper.writeValueAsString(updates);
//        UpdateScheduleRequest result = UpdateScheduleRequest.builder().build();
//
//        // when
//        doReturn(result).when(scheduleService).updateSchedule(any());
//        // then
//        mvc.perform(patch(BASE_URL)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(value))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
//    }
}