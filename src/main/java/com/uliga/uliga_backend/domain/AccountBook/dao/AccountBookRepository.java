package com.uliga.uliga_backend.domain.AccountBook.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.*;
import com.uliga.uliga_backend.domain.AccountBook.model.AccountBook;
import com.uliga.uliga_backend.domain.Category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface AccountBookRepository extends JpaRepository<AccountBook, Long>{

    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "WHERE abm.member.id=:id ORDER BY ab.isPrivate DESC")
    List<AccountBookInfoQ> findAccountBookInfosByMemberId(@Param("id") Long id);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookInfoQ(" +
            "abm.accountBook.id, " +
            "ab.isPrivate, " +
            "ab.name, " +
            "abm.accountBookAuthority, " +
            "abm.getNotification," +
            "ab.relationShip) from " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id WHERE ab.id=:id and abm.member.id = :memberId")
    AccountBookInfoQ findAccountBookInfoById(@Param("id") Long id, @Param("memberId") Long memberId);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookMemberInfoQ(" +
            "m.id, " +
            "m.userName," +
            "abm.accountBookAuthority, " +
            "abm.avatarUrl, " +
            "m.email) " +
            "FROM " +
            "AccountBook ab join AccountBookMember abm on ab.id = abm.accountBook.id " +
            "join Member m on abm.member.id = m.id " +
            "WHERE abm.accountBook.id=:id AND m.deleted=false ")
    List<AccountBookMemberInfoQ> findAccountBookMemberInfoById(@Param("id") Long id);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryInfoQ(" +
            "c.id," +
            "c.name)" +
            "FROM " +
            "AccountBook ab JOIN Category c on c.accountBook.id = ab.id WHERE ab.id = :id")
    List<AccountBookCategoryInfoQ> findAccountBookCategoryInfoById(@Param("id") Long id);
    @Query("SELECT ab " +
            "FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "WHERE abm.member.id=:id")
    List<AccountBook> findAccountBooksByMemberId(@Param("id") Long id);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MembersQ(" +
            "COUNT(abm.member.id)" +
            ") FROM AccountBook ab " +
            "JOIN AccountBookMember abm ON abm.accountBook.id = ab.id " +
            "JOIN Member m ON abm.member.id = m.id " +
            "WHERE abm.accountBook.id=:id AND m.deleted = false GROUP BY ab.id")
    MembersQ getMemberNumberByAccountBookId(@Param("id") Long id);

    void deleteById(Long id);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.DailyValueQ(" +
            "i.date.day, " +
            "SUM(i.value)) " +
            "FROM AccountBook ab " +
            "JOIN Income i ON i.accountBook.id = ab.id " +
            "WHERE i.date.year = :year " +
            "AND i.date.month = :month " +
            "AND ab.id = :id " +
            "GROUP BY i.date " +
            "ORDER BY i.date.day ASC")
    List<DailyValueQ> getMonthlyIncome(@Param("id") Long id,
                                       @Param("year") Long year,
                                       @Param("month") Long month);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.DailyValueQ(" +
            "r.date.day, " +
            "SUM(r.value)) " +
            "FROM AccountBook ab " +
            "JOIN Record r ON r.accountBook.id = ab.id " +
            "WHERE r.date.year = :year " +
            "AND r.date.month = :month " +
            "AND ab.id = :id " +
            "GROUP BY r.date " +
            "ORDER BY r.date.day ASC")
    List<DailyValueQ> getMonthlyRecord(@Param("id") Long id,
                                       @Param("year") Long year,
                                       @Param("month") Long month);

    @Query("SELECT ab FROM AccountBook ab where ab.id in (:accountBookIds)")
    List<AccountBook> findAccountBookByAccountBookIds(@Param("accountBookIds") List<Long> accountBookIds);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.AccountBookCategoryAnalyzeQ(" +
            "c.id, " +
            "c.name, " +
            "SUM(r.value)) " +
            "FROM Category c " +
            "JOIN Record r ON r.category.id = c.id " +
            "WHERE c.accountBook.id = :id  " +
            "AND r.date.year = :year " +
            "AND r.date.month=:month " +
            "group by c " +
            "order by SUM(r.value) DESC LIMIT 5")
    List<AccountBookCategoryAnalyzeQ> findAccountBookCategoryAnalyze(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(sm.value)) " +
            "FROM Schedule s " +
            "JOIN ScheduleMember sm ON sm.schedule.id = s.id " +
            "WHERE s.accountBook.id=:accountBookId " +
            "AND sm.member.id=:memberId " +
            "AND sm.value > 0")
    MonthlySumQ getMonthlyScheduleValue(@Param("accountBookId") Long accountBookId, @Param("memberId") Long memberId);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlyCompareQ(" +
            "r.date.year, " +
            "r.date.month, " +
            "SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id = :accountBookId " +
            "AND -3L < r.date.year * 12L + r.date.month - :year * 12L - :month " +
            "AND r.date.year * 12L + r.date.month - :year * 12L - :month <= 0L  " +
            "GROUP BY r.date.month " +
            "ORDER BY r.date.year * 12L + r.date.month - :year * 12L - :month DESC LIMIT 3")
    List<MonthlyCompareQ> getMonthlyCompare(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month);

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlyCompareQ(" +
            "r.date.year, " +
            "r.date.month, " +
            "SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id = :accountBookId " +
            "AND -2L <  r.date.year * 12L + r.date.month - :year * 12L - :month " +
            "AND r.date.year * 12L + r.date.month - :year * 12L - :month <= 0L  " +
            "GROUP BY r.date.month " +
            "ORDER BY r.date.year * 12L + r.date.month - :year * 12L - :month DESC LIMIT 2")
    List<MonthlyCompareQ> getMonthlyCompareInDailyAnalyze(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month);
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.WeeklySumQ(SUM(r.value)) " +
            "FROM Record r " +
            "WHERE r.accountBook.id=:accountBookId " +
            "AND r.date.year = :year " +
            "AND r.date.month = :month " +
            "AND :startDay <= r.date.day " +
            "AND r.date.day < :endDay " +
            "GROUP BY r.date.month")
    Optional<WeeklySumQ> getWeeklyRecordSum(@Param("accountBookId") Long accountBookId, @Param("year") Long year, @Param("month") Long month, @Param("startDay") Long startDay, @Param("endDay") Long endDay);
}
