package com.uliga.uliga_backend.domain.Income.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ;
import com.uliga.uliga_backend.domain.Income.model.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long>{

    @Query(
            "select new com.uliga.uliga_backend.domain.Income.dto.NativeQ.IncomeInfoQ(" +
                    "i.id," +
                    "i.value," +
                    "i.payment," +
                    "i.account," +
                    "i.memo," +
                    "i.date.year," +
                    "i.date.month," +
                    "i.date.day," +
                    "m.userName," +
                    "c.name) from Income i " +
                    "JOIN Member m on m.id = i.creator.id " +
                    "JOIN Category c on c.id = i.category.id " +
                    "where i.accountBook.id=:id and i.date.month=:month and i.date.year=:year and i.date.day = :day"
    )
    List<IncomeInfoQ> findByAccountBookId(@Param("id") Long id,
                                          @Param("year") Long year,
                                          @Param("month") Long month,
                                          @Param("day") Long day);
    @Query("select new com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(i.value)" +
            ") FROM AccountBook ab " +
            "JOIN Income i ON i.accountBook.id = ab.id " +
            "WHERE i.date.month=:month " +
            "AND ab.id=:id " +
            "AND i.date.year = :year GROUP BY ab.id")
    MonthlySumQ getMonthlySumByAccountBookId(@Param("id") Long id,@Param("year") Long year, @Param("month") Long month);

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
            "c.name) from Income i " +
            "JOIN Member m on m.id = i.creator.id " +
            "JOIN Category c on c.id = i.category.id " +
            "WHERE m.id=:id ORDER BY i.date.year*365 + i.date.month*31 + i.date.day DESC ")
    Page<IncomeInfoQ> getMemberIncomes(@Param("id") Long id, Pageable pageable);



}
