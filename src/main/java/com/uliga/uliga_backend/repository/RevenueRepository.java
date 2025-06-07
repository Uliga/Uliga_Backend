package com.uliga.uliga_backend.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.RevenueDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RevenueRepository {
  private final DSLContext dsl;
  private final RevenueDao dao;
}