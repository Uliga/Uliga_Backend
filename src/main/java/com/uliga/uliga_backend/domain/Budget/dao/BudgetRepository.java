package com.uliga.uliga_backend.domain.Budget.dao;

import com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ;
import com.uliga.uliga_backend.domain.Budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBook.dto.NativeQ.MonthlySumQ(" +
            "SUM(b.value)" +
            ") FROM AccountBook ab " +
            "JOIN Budget b ON b.accountBook.id = ab.id " +
            "WHERE ab.id=:id " +
            "AND b.month=:month " +
            "AND b.year = :year " +
            "GROUP BY ab.id")
    MonthlySumQ getMonthlySumByAccountBookId(@Param("id") Long id, @Param("year") Long year, @Param("month") Long month);

    Optional<Budget> findByAccountBookIdAndYearAndMonth(Long accountBookId, Long year, Long month);
}
