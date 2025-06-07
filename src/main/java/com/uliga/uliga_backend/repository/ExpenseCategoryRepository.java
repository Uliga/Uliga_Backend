package com.uliga.uliga_backend.repository;

import java.util.List;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uliga.uliga_backend.entity.ExpenseCategoryEntity;
import com.uliga.uliga_backend.jooq.tables.daos.ExpenseCategoryDao;
import com.uliga.uliga_backend.jooq.tables.pojos.ExpenseCategory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExpenseCategoryRepository {
  private final DSLContext dsl;
  private final ExpenseCategoryDao dao;

  @Transactional
  public ExpenseCategoryEntity save(ExpenseCategoryEntity entity) {
    ExpenseCategory expenseCategory = new ExpenseCategory(entity);
    dao.insert(expenseCategory);
    return new ExpenseCategoryEntity(expenseCategory);
  }

  @Transactional
  public Stream<ExpenseCategoryEntity> saveAll(List<ExpenseCategoryEntity> entities) {
    List<ExpenseCategory> categories = entities.stream().map((category) -> new ExpenseCategory(category)).toList();
    dao.insert(categories);
    return categories.stream().map((category) -> new ExpenseCategoryEntity(category));
  }
}
