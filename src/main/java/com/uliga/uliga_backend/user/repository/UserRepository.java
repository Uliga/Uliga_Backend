package com.uliga.uliga_backend.domain.user.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uliga.uliga_backend.jooq.tables.daos.UserDao;
import com.uliga.uliga_backend.jooq.tables.pojos.User;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final DSLContext dsl;
  private final UserDao dao;

  @Transactional
  public Mono<User> save(User user) {

  }
}