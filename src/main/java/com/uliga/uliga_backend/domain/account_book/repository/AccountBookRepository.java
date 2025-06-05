package com.uliga.uliga_backend.domain.account_book.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.AccountBookDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountBookRepository {
  private final AccountBookDao dao;
  private final DSLContext dsl;
}
