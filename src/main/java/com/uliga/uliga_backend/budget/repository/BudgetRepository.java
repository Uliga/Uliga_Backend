package com.uliga.uliga_backend.budget.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.BudgetDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BudgetRepository {
  private final BudgetDao dao;
  private final DSLContext dsl;

  /*
   * @Query("SELECT NEW com.uliga.uliga_backend.domain.AccountBookData.dto.NativeQ.MonthlySumQ("
   * +
   * "SUM(b.value)" +
   * ") FROM AccountBook ab " +
   * "JOIN Budget b ON b.accountBook.id = ab.id " +
   * "WHERE ab.id=:id " +
   * "AND b.month=:month " +
   * "AND b.year = :year " +
   * "GROUP BY ab.id")
   * 
   * Optional<MonthlySumQ> getMonthlySumByAccountBookId(@Param("id") Long
   * id, @Param("year") Long year, @Param("month") Long month);
   * 
   * boolean existsBudgetByAccountBookIdAndYearAndMonth(Long accountBookId, Long
   * year, Long month);
   * 
   * Optional<Budget> findByAccountBookIdAndYearAndMonth(Long accountBookId, Long
   * year, Long month);
   * 
   */
}
