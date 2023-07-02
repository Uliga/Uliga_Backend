package com.uliga.uliga_backend.domain.Record.mapper;

import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface RecordMapper {
    List<RecordInfoQ> findAccountBookMemberRecords(HashMap<String, Object> map);

    List<Long> countQueryForRecordHistory(HashMap<String, Object> map);
}
