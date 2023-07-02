package com.uliga.uliga_backend.domain.Income.repository;

import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.DailyValueQ;
import com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Income.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.DailyValueQ(" +
            "i.date.day, " +
            "SUM(i.value)) " +
            "FROM Income i " +
            "WHERE i.accountBook.id = :id " +
            "AND i.date.year = :year " +
            "AND i.date.month = :month " +
            "GROUP BY i.date.year, i.date.month, i.date " +
            "ORDER BY i.date.day ASC")
    List<DailyValueQ> getDailyIncomeSumOfMonth(@Param("id") Long id,
                                       @Param("year") Long year,
                                       @Param("month") Long month);


    @Query("select new com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.MonthlySumQ(" +
            "SUM(i.value)" +
            ") FROM AccountBook ab " +
            "JOIN Income i ON i.accountBook.id = ab.id " +
            "WHERE i.date.month=:month " +
            "AND ab.id=:id " +
            "AND i.date.year = :year GROUP BY ab.id")
    Optional<MonthlySumQ> getMonthlySumByAccountBookId(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month);

    @Query("select new com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ(" +
            "i.id," +
            "i.value," +
            "i.payment," +
            "i.account," +
            "i.memo," +
            "i.date.year," +
            "i.date.month," +
            "i.date.day," +
            "m.userName," +
            "c.name," +
            "abm.avatarUrl) from Income i " +
            "JOIN Member m on m.id = i.creator.id " +
            "JOIN Category c on c.id = i.category.id JOIN AccountBookMember abm ON abm.accountBook.id = i.accountBook.id AND abm.member.id = i.creator.id " +
            "WHERE m.id=:id ORDER BY i.date.year*365 + i.date.month*31 + i.date.day DESC ")
    Page<IncomeInfoQ> getMemberIncomes(@Param("id") Long id, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM income where account_book_data_id = :id")
    void deleteByAccountBookDataIdNativeQ(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO income (account_book_data_id) value (:id)")
    void createFromAccountBookDataId(@Param("id") Long id);
}
