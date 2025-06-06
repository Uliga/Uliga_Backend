package com.uliga.uliga_backend.domain.user.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.uliga.uliga_backend.jooq.tables.daos.UserDao;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final DSLContext dsl;
  private final UserDao dao;
}