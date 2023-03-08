package com.uliga.uliga_backend.domain.Record.dao;

import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    // TODO 기본 카테고리 생성해야할듯??
    @Query(
            "SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ(" +
                    "r.id," +
                    "r.spend," +
                    "r.payment," +
                    "r.account," +
                    "r.memo," +
                    "r.date.year," +
                    "r.date.month," +
                    "r.date.day," +
                    "m.nickName," +
                    "c.name) from Record r " +
                    "JOIN Member m on m.id = r.creator.id " +
                    "JOIN Category c on c.id=r.category.id " +
                    "WHERE r.accountBook.id=:id"
    )
    List<RecordInfoQ> findByAccountBookId(@Param("id") Long id);
}
