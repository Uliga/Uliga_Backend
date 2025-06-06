package com.uliga.uliga_backend.fixed_revenue.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.FixedRevenueDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FixedRevenueRepository {
  private final DSLContext dsl;
  private final FixedRevenueDao dao;
}