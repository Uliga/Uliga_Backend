package com.uliga.uliga_backend.domain.record.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.uliga.uliga_backend.domain.record.dto.NativeQ.RecordInfoQ;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface RecordMapper {
    List<RecordInfoQ> findAccountBookMemberRecords(HashMap<String, Object> map);

    List<Long> countQueryForRecordHistory(HashMap<String, Object> map);
}
