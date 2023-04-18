package com.uliga.uliga_backend.domain.Record.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query(
            "SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ(" +
                    "r.id," +
                    "r.value," +
                    "r.payment," +
                    "r.account," +
                    "r.memo," +
                    "r.date.year," +
                    "r.date.month," +
                    "r.date.day," +
                    "m.userName," +
                    "c.name," +
                    "abm.avatarUrl) from Record r " +
                    "JOIN Member m on m.id = r.creator.id " +
                    "JOIN AccountBookMember abm ON abm.accountBook.id = r.accountBook.id AND abm.member.id = r.creator.id " +
                    "JOIN Category c on c.id=r.category.id " +
                    "WHERE m.id = :id order by r.date.year*365 + r.date.month*31 + r.date.day DESC"
    )
    Page<RecordInfoQ> getMemberRecords(@Param("id") Long id, Pageable pageable);
    @Query(
            "SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ(" +
                    "r.id," +
                    "r.value," +
                    "r.payment," +
                    "r.account," +
                    "r.memo," +
                    "r.date.year," +
                    "r.date.month," +
                    "r.date.day," +
                    "m.userName," +
                    "c.name," +
                    "abm.avatarUrl) from Record r " +
                    "JOIN Member m on m.id = r.creator.id " +
                    "JOIN AccountBookMember abm ON abm.accountBook.id = r.accountBook.id AND abm.member.id = r.creator.id " +
                    "JOIN Category c on c.id=r.category.id " +
                    "WHERE r.accountBook.id=:id and r.date.month=:month and r.date.year = :year and r.date.day = :day"
    )
    List<RecordInfoQ> findByAccountBookId(@Param("id") Long id,
                                          @Param("year") Long year,
                                          @Param("month") Long month,
                                          @Param("day") Long day);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(r.value)" +
            ") FROM AccountBook ab " +
            "JOIN Record r on r.accountBook.id = ab.id " +
            "WHERE ab.id=:id " +
            "AND r.date.month=:month " +
            "AND r.date.year = :year GROUP BY ab.id")
    MonthlySumQ getMonthlySumByAccountBookId(@Param("id") Long id,@Param("year") Long year, @Param("month") Long month);
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM record where account_book_data_id = :id")
    void deleteByAccountBookDataIdNativeQ(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO record (account_book_data_id) value (:id)")
    void createFromAccountBookDataId(@Param("id") Long id);

}
