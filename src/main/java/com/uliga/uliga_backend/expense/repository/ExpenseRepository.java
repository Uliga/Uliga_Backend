package com.uliga.uliga_backend.expense.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.ExpenseDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseRepository {
  private final DSLContext dsl;
  private final ExpenseDao dao;
}
