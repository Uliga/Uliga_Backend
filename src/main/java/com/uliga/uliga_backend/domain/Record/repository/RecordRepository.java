package com.uliga.uliga_backend.domain.Record.repository;

import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.DailyValueQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlyCompareQ;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.WeeklySumQ;
import com.uliga.uliga_backend.domain.Record.dto.NativeQ.RecordInfoQ;
import com.uliga.uliga_backend.domain.Record.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.DailyValueQ(" +
            "r.date.day, " +
            "SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id = :id " +
            "AND r.date.year = :year " +
            "AND r.date.month = :month " +
            "GROUP BY r.date " +
            "ORDER BY r.date.day ASC")
    List<DailyValueQ> getDailyRecordSumOfMonth(@Param("id") Long id,
                                               @Param("year") Long year,
                                               @Param("month") Long month);

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

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.MonthlySumQ(" +
            "SUM(r.value)" +
            ") FROM AccountBook ab " +
            "JOIN Record r on r.accountBook.id = ab.id " +
            "WHERE ab.id=:id " +
            "AND r.date.month=:month " +
            "AND r.date.year = :year GROUP BY ab.id")
    Optional<MonthlySumQ> getMonthlySumByAccountBookId(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month);
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM record where account_book_data_id = :id")
    void deleteByAccountBookDataIdNativeQ(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO record (account_book_data_id) value (:id)")
    void createFromAccountBookDataId(@Param("id") Long id);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlyCompareQ(" +
            "r.date.year, " +
            "r.date.month, " +
            "SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id = :accountBookId " +
            "AND r.date.year = :year AND r.date.month = :month " +
            "GROUP BY r.date.year, r.date.month ")
    Optional<MonthlyCompareQ> getMonthlyCompare(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.MonthlyCompareQ(" +
            "r.date.year, " +
            "r.date.month, " +
            "SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id = :accountBookId " +
            "AND -2L <  r.date.year * 12L + r.date.month - :year * 12L - :month " +
            "AND r.date.year * 12L + r.date.month - :year * 12L - :month <= 0L  " +
            "GROUP BY r.date.year, r.date.month " +
            "ORDER BY r.date.year * 12L + r.date.month - :year * 12L - :month DESC LIMIT 2")
    List<MonthlyCompareQ> getMonthlyCompareInDailyAnalyze(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.Record.dto.NativeQ.WeeklySumQ(SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id=:accountBookId " +
            "AND r.date.year = :year " +
            "AND r.date.month = :month " +
            "AND :startDay <= r.date.day " +
            "AND r.date.day < :endDay " +
            "GROUP BY r.date.year, r.date.month")
    Optional<WeeklySumQ> getWeeklyRecordSum(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month, @Param("startDay") Long startDay, @Param("endDay") Long endDay);

}
