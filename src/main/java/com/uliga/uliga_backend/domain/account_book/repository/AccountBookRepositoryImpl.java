package com.uliga.uliga_backend.domain.account_book.repository;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountBookRepositoryImpl implements AccountBookCustomRepository {
  private final R2dbcEntityTemplate template;

}
