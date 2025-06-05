package com.uliga.uliga_backend.domain.account_book_data.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.AccountBookDataDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountBookDataRepository {
  private final DSLContext dsl;
  private final AccountBookDataDao dao;
}
