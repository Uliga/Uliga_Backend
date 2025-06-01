package com.uliga.uliga_backend.domain.account_book_data.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.uliga.uliga_backend.domain.account_book_data.dto.NativeQ.AccountBookDataQ;

@Mapper
public interface AccountBookDataMapper {
    List<AccountBookDataQ> findAccountBookData(HashMap<String, Object> map);

    List<AccountBookDataQ> findAccountBookDataAnalyze(HashMap<String, Object> map);

    List<AccountBookDataQ> findExtraAccountBookDataAnalyze(HashMap<String, Object> map);

    List<Long> countQueryForExtraAccountBookDataAnalyze(HashMap<String, Object> map);

    List<Long> countQueryForAccountBookDataAnalyze(HashMap<String, Object> map);

    List<AccountBookDataQ> findAccountBookDataOrderByValue(HashMap<String, Object> map);

    List<Long> countQueryForAccountBookHistory(HashMap<String, Object> map);

    List<AccountBookDataQ> findCustomAccountBookData(HashMap<String, Object> map);

    List<Long> countQueryForCustomAccountBookData(HashMap<String, Object> map);
}
