package com.uliga.uliga_backend.domain.income.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uliga.uliga_backend.domain.income.dto.NativeQ.IncomeInfoQ;

@Mapper
public interface IncomeMapper {
    List<IncomeInfoQ> findAccountBookMemberIncomes(HashMap<String, Object> map);

    List<Long> countQueryForIncomeHistory(HashMap<String, Object> map);
}
