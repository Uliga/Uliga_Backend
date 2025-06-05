package com.uliga.uliga_backend.domain.category.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.CategoryDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
  private final DSLContext dsl;
  private final CategoryDao dao;
}