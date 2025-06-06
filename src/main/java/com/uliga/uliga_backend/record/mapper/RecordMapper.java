package com.uliga.uliga_backend.domain.record.mapper;

import java.util.HashMap;
import java.util.List;

import com.uliga.uliga_backend.domain.record.dto.NativeQ.RecordInfoQ;

public interface RecordMapper {
  List<RecordInfoQ> findAccountBookMemberRecords(HashMap<String, Object> map);

  List<Long> countQueryForRecordHistory(HashMap<String, Object> map);
}
