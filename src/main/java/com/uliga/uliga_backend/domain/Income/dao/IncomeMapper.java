package com.uliga.uliga_backend.domain.Income.dao;

import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface IncomeMapper {
    List<IncomeInfoQ> findAccountBookMemberIncomes(HashMap<String, Object> map);

    List<Long> countQueryForIncomeHistory(HashMap<String, Object> map);
}
