package com.uliga.uliga_backend.domain.Record.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
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
                    "WHERE r.accountBook.id=:id and r.date.month=:month"
    )
    List<RecordInfoQ> findByAccountBookId(@Param("id") Long id, @Param("month") Long month);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(r.spend)" +
            ") FROM AccountBook ab " +
            "JOIN Record r on r.accountBook.id = ab.id " +
            "WHERE ab.id=:id " +
            "AND r.date.month=:month " +
            "GROUP BY ab.id")
    MonthlySumQ getMonthlySumByAccountBookId(@Param("id") Long id, @Param("month") Long month);
}
