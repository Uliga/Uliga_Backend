package com.uliga.uliga_backend.domain.fixed_expense.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.FixedExpenseDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FixedExpenseRepository {
  private final DSLContext dsl;
  private final FixedExpenseDao dao;
}