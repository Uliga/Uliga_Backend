package com.uliga.uliga_backend.domain.Record.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Record.application.RecordService;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.dto.RecordDTO.RecordUpdateRequest;
import com.uliga.uliga_backend.global.common.annotation.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecordController.class)
@MockBean(JpaMetamodelMappingContext.class)
class RecordControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    RecordService recordService;

    private final String BASE_URL = "/record";


    @Test
    @WithMockCustomUser
    @DisplayName("지출 업데이트 성공 테스트")
    void updateRecordTestToSuccess() throws Exception {
        // given
        Map<String, Object> updates = new HashMap<>();
        String value = mapper.writeValueAsString(updates);
        RecordUpdateRequest result = RecordUpdateRequest.builder().build();


        // when
        doReturn(result).when(recordService).updateRecord(any());
        // then
        mvc.perform(patch(BASE_URL)
                        .with(csrf())
                        .content(value)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 지출 가계부별 조회 성공 테스트")
    void getMemberRecords() throws Exception {
        // given
        Page<RecordInfoQ> result = new PageImpl<>(new ArrayList<>());

        // when
        doReturn(result).when(recordService).getMemberRecordsByAccountBook(any(), any(), any(), any(), any());
        // then
        mvc.perform(get(BASE_URL + "/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 지출 카테고리별 조회 성공 테스트")
    void getMemberRecordsByCategory() throws Exception {
        // given
        Page<RecordInfoQ> result = new PageImpl<>(new ArrayList<>());

        // when
        doReturn(result).when(recordService).getMemberRecordsByAccountBook(any(), any(), any(), any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1/기타")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
    }
}