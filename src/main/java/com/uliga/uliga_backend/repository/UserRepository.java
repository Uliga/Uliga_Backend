package com.uliga.uliga_backend.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uliga.uliga_backend.entity.UserEntity;
import com.uliga.uliga_backend.jooq.tables.daos.UserDao;
import com.uliga.uliga_backend.jooq.tables.pojos.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final DSLContext dsl;
  private final UserDao dao;

  @Transactional
  public UserEntity save(UserEntity userEntity) {
    User user = new User(userEntity);
    dao.insert(user);
    return new UserEntity(user);
  }
}