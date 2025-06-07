package com.uliga.uliga_backend.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.AccountBookUserDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AccountBookUserRepository {
  private final DSLContext dsl;
  private final AccountBookUserDao dao;
}
