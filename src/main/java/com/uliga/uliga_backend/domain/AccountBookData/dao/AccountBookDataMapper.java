package com.uliga.uliga_backend.domain.AccountBookData.dao;

import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.AccountBookDataQ;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AccountBookDataMapper {
    List<AccountBookDataQ> findAccountBookData(HashMap<String, Object> map);

    List<AccountBookDataQ> findAccountBookDataAnalyze(HashMap<String, Object> map);

    List<AccountBookDataQ> findExtraAccountBookDataAnalyze(HashMap<String, Object> map);

    List<Long> countQueryForExtraAccountBookDataAnalyze(HashMap<String, Object> map);

    List<Long> countQueryForAccountBookDataAnalyze(HashMap<String, Object> map);

    List<AccountBookDataQ> findAccountBookDataOrderByValue(HashMap<String, Object> map);

    List<Long> countQueryForAccountBookHistory(HashMap<String, Object> map);
}
