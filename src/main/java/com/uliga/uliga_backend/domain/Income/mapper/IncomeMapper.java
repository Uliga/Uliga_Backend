package com.uliga.uliga_backend.domain.income.mapper;

import java.util.HashMap;
import java.util.List;

import com.uliga.uliga_backend.domain.income.dto.NativeQ.IncomeInfoQ;

public interface IncomeMapper {
  List<IncomeInfoQ> findAccountBookMemberIncomes(HashMap<String, Object> map);

  List<Long> countQueryForIncomeHistory(HashMap<String, Object> map);
}
