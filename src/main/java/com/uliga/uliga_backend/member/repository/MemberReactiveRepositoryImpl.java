package com.uliga.uliga_backend.domain.member.repository;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberReactiveRepositoryImpl implements MemberCustomRepository {
  private final R2dbcEntityTemplate template;
}
