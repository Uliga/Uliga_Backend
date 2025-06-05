package com.uliga.uliga_backend.domain.budget.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.BudgetDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BudgetRepository {
  private final BudgetDao dao;
  private final DSLContext dsl;
}
