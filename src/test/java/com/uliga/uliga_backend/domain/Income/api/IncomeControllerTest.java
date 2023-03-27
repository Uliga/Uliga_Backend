package com.uliga.uliga_backend.domain.Income.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uliga.uliga_backend.domain.Income.application.IncomeService;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
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

import static com.uliga.uliga_backend.domain.Income.dto.IncomeDTO.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@WebMvcTest(IncomeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class IncomeControllerTest {

    @Autowired
    ObjectMapper mapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    IncomeService incomeService;

    private final String BASE_URL = "/income";


    @Test
    @WithMockCustomUser
    @DisplayName("수입 업데이트 성공 테스트")
    public void updateIncomeTestToSuccess() throws Exception{
        // given
        Map<String, Object> updates = new HashMap<>();
        String value = mapper.writeValueAsString(updates);
        IncomeUpdateRequest result = IncomeUpdateRequest.builder().build();

        // when
        doReturn(result).when(incomeService).updateIncome(any());
        // then
        mvc.perform(patch(BASE_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(value))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("멤버 수입 가계부별 조회 성공 테스트")
    public void getMemberIncomesTestToSuccess() throws Exception{
        // given
        Page<IncomeInfoQ> result = new PageImpl<>(new ArrayList<>());

        // when
        doReturn(result).when(incomeService).getMemberIncomesByAccountBook(any(), any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("수입 카테고리 별 조회 성공 테스트")
    public void getMemberIncomeWithCategoryTestToSuccess() throws Exception{
        // given
        Page<IncomeInfoQ> result = new PageImpl<>(new ArrayList<>());

        // when
        doReturn(result).when(incomeService).getMemberIncomesByCategory(any(), any(), any(), any());

        // then
        mvc.perform(get(BASE_URL + "/1/기타")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString().equals(mapper.writeValueAsString(result));
    }


}